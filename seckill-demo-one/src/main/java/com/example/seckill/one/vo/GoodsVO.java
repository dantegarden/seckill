package com.example.seckill.one.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class GoodsVO {
    private Long id;
    private String goodsName;
    private String goodsTitle;
    private String goodsImg;
    private String goodsDetail;
    private BigDecimal goodsPrice;
    private Integer goodsStock;

    private BigDecimal seckillPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
