package com.example.seckill.one.vo;

import com.example.seckill.one.model.entity.User;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GoodsDetailVO {

    private GoodsVO goods;
    private User user;
    private int seckillStatus = 0;
    private int remainSeconds = 0;
}
