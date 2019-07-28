package com.example.seckill.one.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.seckill.one.model.entity.Goods;
import com.example.seckill.one.model.entity.SeckillGoods;
import com.example.seckill.one.vo.GoodsVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeckillGoodsMapper extends BaseMapper<SeckillGoods> {

}
