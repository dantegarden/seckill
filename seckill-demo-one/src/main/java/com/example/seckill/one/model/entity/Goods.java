package com.example.seckill.one.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("tb_goods")
public class Goods {

    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;
    private String goodsName;
    private String goodsTitle;
    private String goodsImg;
    private String goodsDetail;
    private BigDecimal goodsPrice;
    private Integer goodsStock;
}
