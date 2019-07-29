package com.example.seckill.one.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("tb_seckill_goods")
public class SeckillGoods {

    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;
    private Long goodsId;
    private BigDecimal seckillPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
    @Version
    private Integer version;
}