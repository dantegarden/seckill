package com.example.seckill.one.redis;

public abstract class KeyPrefix {

    private static final String KEY_TEMPLATE = "%s:%s";

    private int expireSeconds;
    private String prefix;

    public KeyPrefix(String prefix) {//0代表永不过期
        this(prefix,0);
    }

    public KeyPrefix(String prefix, int expireSeconds) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    public int expireSeconds() {//默认0代表永不过期
        return expireSeconds;
    }

    public String getPrefix() {
        String className = getClass().getSimpleName();
        return String.format(KEY_TEMPLATE, className, prefix);
    }
}
