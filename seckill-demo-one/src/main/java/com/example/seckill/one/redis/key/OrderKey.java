package com.example.seckill.one.redis.key;

import com.example.seckill.one.redis.KeyPrefix;

public class OrderKey extends KeyPrefix {
    public static final String REDIS_UID_GID_TEMPLATE = "UID_GID";

    public OrderKey(String prefix) {
        super(prefix);
    }


    public static OrderKey UID_GID = new OrderKey(REDIS_UID_GID_TEMPLATE);
}
