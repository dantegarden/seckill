package com.example.seckill.one.vo;

import com.example.seckill.one.model.entity.User;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class GoodsListVO {

    private List<GoodsVO> goodsList;
    private User user;
}
