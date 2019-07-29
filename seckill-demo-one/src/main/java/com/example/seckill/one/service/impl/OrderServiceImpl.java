package com.example.seckill.one.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.seckill.one.mapper.OrderMapper;
import com.example.seckill.one.mapper.SeckillOrderMapper;
import com.example.seckill.one.model.entity.Order;
import com.example.seckill.one.model.entity.SeckillOrder;
import com.example.seckill.one.model.entity.User;
import com.example.seckill.one.redis.RedisService;
import com.example.seckill.one.redis.key.OrderKey;
import com.example.seckill.one.service.OrderService;
import com.example.seckill.one.service.UserService;
import com.example.seckill.one.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public SeckillOrder fetchSeckillOrderByUserIdAndGoodsId(Long userId, Long goodsId) {
//        QueryWrapper<SeckillOrder> queryWrapper = Wrappers.<SeckillOrder>query();
////        queryWrapper.eq("goods_id", goodsId).eq("user_id", userId);
////        SeckillOrder order = seckillOrderMapper.selectOne(queryWrapper);
        SeckillOrder order =  redisService.get(OrderKey.UID_GID, userId+"_"+goodsId, SeckillOrder.class);
        return order;
    }

    @Transactional
    @Override
    public Order createOrder(User user, GoodsVO goods) {
        Order newOrder = new Order();
        newOrder.setCreateDate(new Date())
                .setUserId(user.getId())
                .setGoodsId(goods.getId())
                .setDeliveryAddrId(0L)
                .setGoodsCount(1)
                .setGoodsName(goods.getGoodsName())
                .setGoodsPrice(goods.getSeckillPrice())
                .setOrderChannel(1)
                .setOrderStatus(0);
        orderMapper.insert(newOrder);
        Long orderId = newOrder.getId();

        SeckillOrder newSeckillOrder = new SeckillOrder();
        newSeckillOrder.setOrderId(orderId)
                .setUserId(user.getId())
                .setGoodsId(goods.getId());
        seckillOrderMapper.insert(newSeckillOrder);

        //缓存秒杀订单对象，永不过期
        redisService.set(OrderKey.UID_GID, user.getId()+"_"+goods.getId(), newSeckillOrder);

        return newOrder;
    }

    @Override
    public void reset() {
        orderMapper.delete((QueryWrapper)Wrappers.<Order>query().isNotNull("id"));
        seckillOrderMapper.delete((QueryWrapper)Wrappers.<SeckillOrder>query().isNotNull("id"));
    }
}
