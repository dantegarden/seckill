package com.example.seckill.one.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.seckill.one.enums.ResultSuccessEnum;
import com.example.seckill.one.mapper.GoodsMapper;
import com.example.seckill.one.model.entity.*;
import com.example.seckill.one.redis.RedisService;
import com.example.seckill.one.redis.key.GoodsKey;
import com.example.seckill.one.redis.key.SeckillKey;
import com.example.seckill.one.service.GoodsService;
import com.example.seckill.one.service.OrderService;
import com.example.seckill.one.service.SeckillService;
import com.example.seckill.one.utils.VerifyCodeUtils;
import com.example.seckill.one.vo.GoodsVO;
import com.example.seckill.one.vo.SeckillResultVO;
import com.example.seckill.one.vo.VerifyCodeVO;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

@Service
@Transactional
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisService redisService;

    @Override
    public Order seckill(User user, GoodsVO goods) {
        //减库存 如果失败会抛异常，整体回滚
        goodsService.reduceStock(goods);
        //下订单
        return orderService.createOrder(user, goods);
    }

    @Override
    public void setGoodsOver(Long goodsId) {
        redisService.set(GoodsKey.IS_GOODS_OVER, goodsId.toString(), Boolean.TRUE);
    }

    @Override
    public boolean isGoodsOver(Long goodsId) {
        return redisService.exists(GoodsKey.IS_GOODS_OVER, goodsId.toString());
    }

    @Override
    public void reset(List<GoodsVO> goodsVOList) {
        List<Goods> goodsList = Lists.newArrayList();
        List<SeckillGoods> seckillGoodsList = Lists.newArrayList();
        for (GoodsVO goods: goodsVOList) {
            Goods _goods = new Goods();
            _goods.setId(goods.getId()).setGoodsStock(goods.getGoodsStock());
            goodsList.add(_goods);
            SeckillGoods seckillGoods = goodsService.fetchByGoodsId(goods.getId());
            seckillGoods.setStockCount(goods.getStockCount()).setVersion(seckillGoods.getVersion());
            seckillGoodsList.add(seckillGoods);
        }
        goodsService.updateBatchById(goodsList);
        goodsService.updateSeckillGoodsBatchByIds(seckillGoodsList);

        orderService.reset();
    }

    @Override
    public BufferedImage createVerifyCode(User user, Long goodsId) {
        VerifyCodeVO verifyCode = VerifyCodeUtils.createVerifyCode();
        //把验证码存到redis中
        int rnd = verifyCode.getCalc();
        //数学公式的值缓存到redis
        redisService.set(SeckillKey.VERIFY_CODE, user.getId()+"_"+goodsId, rnd);
        return verifyCode.getImage();
    }


}
