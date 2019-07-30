package com.example.seckill.one.redis.key;

import com.example.seckill.one.redis.KeyPrefix;

public class SeckillKey extends KeyPrefix {

    public static final String REDIS_SECKILL_PATH_TEMPLATE = "SECKILL_PATH";
    public static final int REDIS_SECKILL_PATH_EXPIRE = 60;

    public static final String REDIS_VERIFY_CODE_TEMPLATE = "VERIFY_CODE";
    public static final int REDIS_VERIFY_CODE_EXPIRE = 60*5;

    public SeckillKey(String prefix) {
        super(prefix);
    }

    public SeckillKey(String prefix, int expireSeconds) {
        super(prefix, expireSeconds);
    }

    public static SeckillKey SECKILL_PATH = new SeckillKey(REDIS_SECKILL_PATH_TEMPLATE, REDIS_SECKILL_PATH_EXPIRE);
    public static SeckillKey VERIFY_CODE = new SeckillKey(REDIS_VERIFY_CODE_TEMPLATE, REDIS_VERIFY_CODE_EXPIRE);
}
