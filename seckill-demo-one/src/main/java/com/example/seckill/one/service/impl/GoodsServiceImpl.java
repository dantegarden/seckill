package com.example.seckill.one.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.seckill.one.mapper.GoodsMapper;
import com.example.seckill.one.mapper.SeckillGoodsMapper;
import com.example.seckill.one.model.entity.Goods;
import com.example.seckill.one.model.entity.SeckillGoods;
import com.example.seckill.one.service.GoodsService;
import com.example.seckill.one.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Override
    public List<GoodsVO> listGoodsVO() {
        return goodsMapper.listGoodsVO();
    }

    @Override
    public GoodsVO fetchGoodsvoById(Long goodsId) {
        return goodsMapper.fetchGoodsvoById(goodsId);
    }

    @Override
    public void reduceStock(GoodsVO goods) {
        //商品总库存减少
        Goods g = new Goods();
        g.setId(goods.getId());
        g.setGoodsStock(goods.getGoodsStock()-1);
        goodsMapper.updateById(g);
        //秒杀商品库存减少
        QueryWrapper queryWrapper = Wrappers.<SeckillGoods>query();
        queryWrapper.eq("goods_id", goods.getId());
        SeckillGoods seckillGoods = new SeckillGoods();
        seckillGoods.setStockCount(goods.getStockCount()-1);
        seckillGoodsMapper.update(seckillGoods, queryWrapper);
    }
}
