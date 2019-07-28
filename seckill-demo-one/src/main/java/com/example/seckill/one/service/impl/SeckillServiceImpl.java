package com.example.seckill.one.service.impl;

import com.example.seckill.one.mapper.GoodsMapper;
import com.example.seckill.one.model.entity.Goods;
import com.example.seckill.one.model.entity.Order;
import com.example.seckill.one.model.entity.User;
import com.example.seckill.one.service.GoodsService;
import com.example.seckill.one.service.OrderService;
import com.example.seckill.one.service.SeckillService;
import com.example.seckill.one.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Override
    public Order seckill(User user, GoodsVO goods) {
        //减库存
        goodsService.reduceStock(goods);
        //下订单
        return orderService.createOrder(user, goods);
    }
}
