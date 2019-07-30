package com.example.seckill.one.redis.key;

import com.example.seckill.one.redis.KeyPrefix;

public class AccessKey extends KeyPrefix {

    public static final String REDIS_ACCESS_LIMIT_TEMPLATE = "ACCESS_LIMIT";
    public static final int REDIS_ACCESS_LIMIT_EXPIRE = 10; //默认存活10秒

    public AccessKey(String prefix) {
        super(prefix);
    }

    public AccessKey(String prefix, int expireSeconds) {
        super(prefix, expireSeconds);
    }

    public static AccessKey ACCESS_LIMIT = new AccessKey(REDIS_ACCESS_LIMIT_TEMPLATE, REDIS_ACCESS_LIMIT_EXPIRE);
    public static AccessKey ACCESS_LIMIT(int expireSeconds){
        return new AccessKey(REDIS_ACCESS_LIMIT_TEMPLATE, expireSeconds);
    }

}
