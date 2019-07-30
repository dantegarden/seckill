package com.example.seckill.one.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckill.one.model.entity.Goods;
import com.example.seckill.one.model.entity.SeckillGoods;
import com.example.seckill.one.vo.GoodsVO;

import java.util.List;

public interface GoodsService extends IService<Goods> {
    List<GoodsVO> listGoodsVO();
    GoodsVO fetchGoodsvoById(Long goodsId);
    SeckillGoods fetchByGoodsId(Long goodsId);
    int updateSeckillGoodsBatchByIds(List<SeckillGoods> seckillGoodsList);

    void reduceStock(GoodsVO goods);

}