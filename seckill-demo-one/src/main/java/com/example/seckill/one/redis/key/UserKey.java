package com.example.seckill.one.redis.key;

import com.example.seckill.one.redis.KeyPrefix;

public class UserKey extends KeyPrefix {

    public static final String REDIS_USER_TOKEN_TEMPLATE = "USER-TOKEN";
    public static final int REDIS_USER_TOKEN_EXPIRE = 3600*24*1;

    public UserKey(String prefix) {
        super(prefix);
    }

    public UserKey(String prefix, int expireSeconds) {
        super(prefix, expireSeconds);
    }

    public static UserKey USER_TOKEN = new UserKey(REDIS_USER_TOKEN_TEMPLATE, REDIS_USER_TOKEN_EXPIRE);
}
