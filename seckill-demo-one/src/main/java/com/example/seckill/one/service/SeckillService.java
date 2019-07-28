package com.example.seckill.one.service;

import com.example.seckill.one.model.entity.Order;
import com.example.seckill.one.model.entity.User;
import com.example.seckill.one.vo.GoodsVO;

public interface SeckillService {
    Order seckill(User user, GoodsVO goods);
}
