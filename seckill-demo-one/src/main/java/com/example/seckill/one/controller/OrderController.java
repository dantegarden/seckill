package com.example.seckill.one.controller;

import com.example.seckill.one.bean.Result;
import com.example.seckill.one.enums.ResultErrorEnum;
import com.example.seckill.one.model.entity.Order;
import com.example.seckill.one.model.entity.User;
import com.example.seckill.one.redis.RedisService;
import com.example.seckill.one.service.GoodsService;
import com.example.seckill.one.service.OrderService;
import com.example.seckill.one.service.UserService;
import com.example.seckill.one.vo.GoodsVO;
import com.example.seckill.one.vo.OrderDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private UserService userService;

	@Autowired
	private RedisService redisService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private GoodsService goodsService;

    @GetMapping("/detail")
    public Result<OrderDetailVO> info(Model model, User user, @RequestParam("orderId") Long orderId) {
    	if(user == null) {
    		return Result.failByEnum(ResultErrorEnum.USER_NOT_LOGIN);
    	}
    	Order order = orderService.getById(orderId);
    	if(order == null) {
    		return Result.failByEnum(ResultErrorEnum.ORDER_NOT_EXIST);
    	}
    	Long goodsId = order.getGoodsId();
    	GoodsVO goods = goodsService.fetchGoodsvoById(goodsId);
    	OrderDetailVO orderDetailVO = new OrderDetailVO();
		orderDetailVO.setOrder(order).setGoods(goods).setUser(user);
    	return Result.ok(orderDetailVO);
    }

}
