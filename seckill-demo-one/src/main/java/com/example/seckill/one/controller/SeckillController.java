package com.example.seckill.one.controller;

import com.example.seckill.one.bean.Result;
import com.example.seckill.one.config.annotation.AccessLimit;
import com.example.seckill.one.enums.ResultErrorEnum;
import com.example.seckill.one.enums.ResultSuccessEnum;
import com.example.seckill.one.model.dto.SeckillRequest;
import com.example.seckill.one.model.entity.SeckillOrder;
import com.example.seckill.one.model.entity.User;
import com.example.seckill.one.mq.MQSender;
import com.example.seckill.one.mq.SeckillMQSender;
import com.example.seckill.one.redis.RedisService;
import com.example.seckill.one.redis.key.GoodsKey;
import com.example.seckill.one.redis.key.OrderKey;
import com.example.seckill.one.redis.key.SeckillKey;
import com.example.seckill.one.service.GoodsService;
import com.example.seckill.one.service.OrderService;
import com.example.seckill.one.service.SeckillService;
import com.example.seckill.one.service.UserService;
import com.example.seckill.one.utils.MD5Utils;
import com.example.seckill.one.vo.GoodsVO;
import com.example.seckill.one.vo.SeckillResultVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SeckillService seckillService;

    @Autowired
    private SeckillMQSender sender;

    @AccessLimit(seconds = 10, maxCount = 10, needLogin = true)
    @PostMapping("/{path}/doSeckill")
    public Result<Enum> doSeckill(Model model, User user, @RequestParam("goodsId") Long goodsId, @PathVariable("path") String path) {

        //防刷，检查path是否有效
        if(!checkPath(user, goodsId, path)){
            return Result.failByEnum(ResultErrorEnum.REQUEST_ILLEGAL);
        }

        //是否已经秒杀到了
        SeckillOrder seckillOrder = orderService.fetchSeckillOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if(seckillOrder != null){
            return Result.failByEnum(ResultErrorEnum.SECKILL_REPEAT);
        }

        //预减库存，返回减后的库存数
        Long remainStock = redisService.decr(GoodsKey.GOODS_STOCK, goodsId.toString(), 1);
        if(remainStock < 0){ //没有库存了，秒杀结束
            return Result.failByEnum(ResultErrorEnum.SECKILL_OVER);
        }

        //秒杀请求入队
        SeckillRequest seckillRequest = new SeckillRequest(user, goodsId);
        sender.sendSeckillRequest(seckillRequest);

        //返回排队中
        return Result.okByEnum(ResultSuccessEnum.SECKILL_PENDING);
    }

    /**获取秒杀结果**/
    @GetMapping("/result")
    public Result<Enum> getSeckillResult(Model model, User user, @RequestParam("goodsId") Long goodsId) {
        //判断用户是否已登录
        if(user == null) {
            return Result.failByEnum(ResultErrorEnum.USER_NOT_LOGIN);
        }

        SeckillOrder seckillOrder = orderService.fetchSeckillOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if(seckillOrder != null){ //秒杀成功，返回订单id
            SeckillResultVO seckillResultVO = new SeckillResultVO();
            seckillResultVO.setOrderId(seckillOrder.getOrderId()).setUser(user);
            return Result.ok(seckillResultVO);
        }

        if(seckillService.isGoodsOver(goodsId)){ //库存没了，没秒杀到
            return Result.okByEnum(ResultSuccessEnum.SECKILL_OVER);
        }else{
            //仍有库存，继续排队
            return Result.okByEnum(ResultSuccessEnum.SECKILL_PENDING);
        }
    }

    /**获取秒杀接口地址**/
    @AccessLimit(seconds = 5, maxCount = 5, needLogin = true) //5秒内只能访问五次，而且必须登录
    @GetMapping("/seckillPath")
    public Result<String> getSeckillPath(Model model, User user,
                                         @RequestParam("goodsId") Long goodsId,
                                         @RequestParam("verifyCode") String verifyCode) {
        //校验验证码
        if(verifyCode==null || !checkVerifyCode(user, goodsId, verifyCode)){
            return Result.failByEnum(ResultErrorEnum.VERIFYCODE_FALUT);
        }
        //生成随机地址
        String seckillPath = MD5Utils.md5(UUID.randomUUID().toString() + "wsadijkl");
        redisService.set(SeckillKey.SECKILL_PATH, user.getId()+ "_" + goodsId, seckillPath);
        return Result.ok(seckillPath);
    }

    /**便于测试接口，还原环境*/
    @GetMapping("/reset/{seckillNum}")
    public Result<Boolean> reset(Model model, @PathVariable Integer seckillNum) {
        List<GoodsVO> goodsList = goodsService.listGoodsVO();
        for(GoodsVO goods: goodsList){
            goods.setGoodsStock(10000);
            goods.setStockCount(seckillNum);
            redisService.set(GoodsKey.GOODS_STOCK, goods.getId().toString(), seckillNum);
        }
        Set<String> orderKeys = redisService.keys(OrderKey.UID_GID);
        for(String orderKey: orderKeys){
            redisService.del(orderKey);
        }
        Set<String> isGoodsOverKeys = redisService.keys(GoodsKey.IS_GOODS_OVER);
        for(String isGoodsOverKey: isGoodsOverKeys){
            redisService.del(isGoodsOverKey);
        }
        seckillService.reset(goodsList);
        return Result.ok(Boolean.TRUE);
    }

    @AccessLimit(seconds = 10, maxCount = 10, needLogin = true)
    @GetMapping(value="/verifyCode")
    @ResponseBody
    public Result<String> getSeckillVerifyCode(HttpServletResponse response, User user,
                                              @RequestParam("goodsId") Long goodsId) {
        try {
            BufferedImage image  = seckillService.createVerifyCode(user, goodsId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return null;
        }catch(Exception e) {
            e.printStackTrace();
            return Result.failByEnum(ResultErrorEnum.SECKILL_BUSY);
        }
    }

    /**系统启动时，加载商品库存到redis**/
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVO> goodsList = goodsService.listGoodsVO();
        if(CollectionUtils.isEmpty(goodsList)){
            return;
        }
        for(GoodsVO goods: goodsList){
            //永不过期
            redisService.set(GoodsKey.GOODS_STOCK, goods.getId().toString(), goods.getStockCount());
        }
    }

    /**校验随机路径**/
    private Boolean checkPath(User user, Long goodsId, String path){
        return (StringUtils.isNotBlank(path)
                && path.equals(redisService.get(SeckillKey.SECKILL_PATH, user.getId()+ "_" + goodsId)));
    }

    /***校验验证码**/
    private Boolean checkVerifyCode(User user, Long goodsId, String verifyCodeStr){
        //类型校验
        try{
            int verifyCode = Integer.valueOf(verifyCodeStr);
        }catch (NumberFormatException e){
            return Boolean.FALSE;
        }

        Integer oldVerifyCode = redisService.get(SeckillKey.VERIFY_CODE, user.getId() + "_" + goodsId, Integer.class);
        if(oldVerifyCode == null || !oldVerifyCode.equals(Integer.valueOf(verifyCodeStr))){
            return Boolean.FALSE;
        }
        //删除缓存，避免重复使用
        redisService.del(SeckillKey.VERIFY_CODE, user.getId() + "_" + goodsId);
        return Boolean.TRUE;
    }
}
