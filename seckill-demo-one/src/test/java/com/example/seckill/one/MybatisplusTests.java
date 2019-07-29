package com.example.seckill.one;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.seckill.one.mapper.SeckillGoodsMapper;
import com.example.seckill.one.mapper.SeckillOrderMapper;
import com.example.seckill.one.model.entity.SeckillGoods;
import com.example.seckill.one.model.entity.SeckillOrder;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisplusTests {

    @Autowired
    SeckillGoodsMapper seckillGoodsMapper;

    @Test
    public void contextLoads() {
        SeckillGoods oldSeckillGoods = seckillGoodsMapper.selectByMap(ImmutableMap.of("goods_id", 1)).get(0);
        QueryWrapper queryWrapper = Wrappers.<SeckillGoods>query();
        queryWrapper.eq("goods_id", 1);
        queryWrapper.gt("stock_count", 0);
        SeckillGoods seckillGoods = new SeckillGoods();
        seckillGoods.setVersion(oldSeckillGoods.getVersion());
        seckillGoods.setStockCount(oldSeckillGoods.getStockCount()-1);
        seckillGoodsMapper.update(seckillGoods, queryWrapper);
    }

}
