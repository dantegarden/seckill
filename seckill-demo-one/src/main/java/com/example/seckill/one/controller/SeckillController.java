package com.example.seckill.one.controller;

import com.example.seckill.one.bean.Result;
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
import com.example.seckill.one.service.GoodsService;
import com.example.seckill.one.service.OrderService;
import com.example.seckill.one.service.SeckillService;
import com.example.seckill.one.service.UserService;
import com.example.seckill.one.vo.GoodsVO;
import com.example.seckill.one.vo.SeckillResultVO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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

    @PostMapping("/doSeckill")
    public Result<Enum> doSeckill(Model model, User user, @RequestParam("goodsId") Long goodsId) {
        //判断用户是否已登录
        if(user == null) {
            return Result.failByEnum(ResultErrorEnum.USER_NOT_LOGIN);
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
}
