package com.example.seckill.one.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.seckill.one.model.entity.Goods;
import com.example.seckill.one.vo.GoodsVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsMapper extends BaseMapper<Goods> {

    public List<GoodsVO> listGoodsVO();

    public GoodsVO fetchGoodsvoById(@Param("goodsId") Long goodsId);
}
