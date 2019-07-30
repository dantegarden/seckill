package com.example.seckill.one.service;

import com.example.seckill.one.enums.ResultSuccessEnum;
import com.example.seckill.one.model.entity.Order;
import com.example.seckill.one.model.entity.User;
import com.example.seckill.one.vo.GoodsVO;
import com.example.seckill.one.vo.SeckillResultVO;

import java.awt.image.BufferedImage;
import java.util.List;

public interface SeckillService {
    Order seckill(User user, GoodsVO goods);

    void setGoodsOver(Long goodsId);

    boolean isGoodsOver(Long goodsId);

    void reset(List<GoodsVO> goodsList);

    BufferedImage createVerifyCode(User user, Long goodsId);
}
