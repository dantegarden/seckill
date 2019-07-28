package com.example.seckill.one.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.seckill.one.model.entity.Goods;
import com.example.seckill.one.model.entity.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMapper extends BaseMapper<Order> {

}
