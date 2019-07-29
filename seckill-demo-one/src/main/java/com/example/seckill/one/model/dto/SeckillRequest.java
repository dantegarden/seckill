package com.example.seckill.one.model.dto;

import com.example.seckill.one.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SeckillRequest {
    private User user;
    private Long goodsId;
}
