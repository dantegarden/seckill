package com.example.seckill.one.vo;

import com.example.seckill.one.model.entity.User;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SeckillResultVO {
    private User user;
    private Long orderId;
}
