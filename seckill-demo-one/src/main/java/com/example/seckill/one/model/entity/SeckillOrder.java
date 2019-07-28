package com.example.seckill.one.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("tb_seckill_order")
public class SeckillOrder extends Model<SeckillOrder> {

    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;
    private Long userId;
    private Long orderId;
    private Long goodsId;
}
