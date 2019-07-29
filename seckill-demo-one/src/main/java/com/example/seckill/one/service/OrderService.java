package com.example.seckill.one.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckill.one.model.entity.Order;
import com.example.seckill.one.model.entity.SeckillOrder;
import com.example.seckill.one.model.entity.User;
import com.example.seckill.one.vo.GoodsVO;

public interface OrderService extends IService<Order> {

    SeckillOrder fetchSeckillOrderByUserIdAndGoodsId(Long userId, Long goodsId);

    Order createOrder(User user, GoodsVO goods);

    /**方便测试，清除两个订单表*/
    void reset();
}
