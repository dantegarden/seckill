package com.example.seckill.one.vo;

import com.example.seckill.one.model.entity.Order;
import com.example.seckill.one.model.entity.User;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrderDetailVO {
    private String errmsg;
    private Order order;
    private GoodsVO goods;
    private User user;
}
