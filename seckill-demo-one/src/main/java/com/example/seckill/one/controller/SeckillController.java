package com.example.seckill.one.controller;

import com.example.seckill.one.bean.Result;
import com.example.seckill.one.enums.ResultErrorEnum;
import com.example.seckill.one.model.entity.Order;
import com.example.seckill.one.model.entity.SeckillOrder;
import com.example.seckill.one.model.entity.User;
import com.example.seckill.one.redis.RedisService;
import com.example.seckill.one.service.GoodsService;
import com.example.seckill.one.service.OrderService;
import com.example.seckill.one.service.SeckillService;
import com.example.seckill.one.service.UserService;
import com.example.seckill.one.vo.GoodsVO;
import com.example.seckill.one.vo.OrderDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seckill")
public class SeckillController {

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

    @PostMapping("/doSeckill")
    public Result<Order> list(Model model, User user, @RequestParam("goodsId") Long goodsId) {
        //判断用户是否已登录
        if(user == null) {
            return Result.failByEnum(ResultErrorEnum.USER_NOT_LOGIN);
        }
        //判断库存
        GoodsVO goods = goodsService.fetchGoodsvoById(goodsId);
        int stock = goods.getStockCount();
        if(stock <= 0) {
            return Result.failByEnum(ResultErrorEnum.SECKILL_OVER);
        }
        //判断是否已经秒杀到了
        SeckillOrder seckillOrder = orderService.fetchSeckillOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if(seckillOrder != null){
            return Result.failByEnum(ResultErrorEnum.SECKILL_REPEAT);
        }

        //减库存 下订单 写入秒杀订单
        Order orderInfo = seckillService.seckill(user, goods);

        return Result.ok(orderInfo);
    }
}
