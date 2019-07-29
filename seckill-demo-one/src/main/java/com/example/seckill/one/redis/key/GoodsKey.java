package com.example.seckill.one.redis.key;

import com.example.seckill.one.redis.KeyPrefix;

public class GoodsKey extends KeyPrefix {
    public static final String REDIS_GOODS_STOCK_TEMPLATE = "GOODS_STOCK";
    public static final String REDIS_IS_GOODS_OVER_TEMPLATE = "IS_GOODS_OVER";
    public GoodsKey(String prefix) {
        super(prefix);
    }


    public static GoodsKey GOODS_STOCK = new GoodsKey(REDIS_GOODS_STOCK_TEMPLATE);
    public static GoodsKey IS_GOODS_OVER = new GoodsKey(REDIS_IS_GOODS_OVER_TEMPLATE);
}
