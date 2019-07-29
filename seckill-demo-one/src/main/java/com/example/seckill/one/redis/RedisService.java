package com.example.seckill.one.redis;

import com.example.seckill.one.utils.FastjsonUtils;
import com.example.seckill.one.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private RedisUtils redisUtils;

    private static final String REAL_KEY_TEMPLATE = "%s:%s";

    private String getRealKey(KeyPrefix prefix, String key){
        return String.format(REAL_KEY_TEMPLATE, prefix.getPrefix(), key);
    }

    /****----------------------------------String----------------------------------------****/

    /**
     * 实现命令：TTL key，以秒为单位，返回给定 key的剩余生存时间(TTL, time to live)。
     * realKey = prefix + key
     */
    public long ttl(KeyPrefix prefix, String key) {
        return redisUtils.ttl(getRealKey(prefix, key));
    }

    /**
     * 实现命令：expire 设置过期时间，单位秒
     * realKey = prefix + key
     */
    public void expire(KeyPrefix prefix, String key, long timeout) {
        redisUtils.expire(getRealKey(prefix, key), timeout);
    }

    /**
     * 实现命令：INCR key，增加key一次
     * realKey = prefix + key
     */
    public long incr(KeyPrefix prefix, String key, long delta) {
        return redisUtils.incr(getRealKey(prefix, key), delta);
    }

    /**
     * 实现命令： key，减少key一次
     * realKey = prefix + key
     */
    public long decr(KeyPrefix prefix, String key, long delta) {
        String realKey  = getRealKey(prefix, key);
        if(delta < 0){
            //throw new RuntimeException("递减因子必须大于0");
            del(realKey);
            return 0;
        }
        return redisUtils.decr(realKey, delta);
    }

    /**
     * 实现命令：KEYS pattern，查找所有符合给定模式 pattern的 key
     * pattern = prefix
     */
    public Set<String> keys(KeyPrefix prefix) {
        String pattern  = prefix.getPrefix();
        return redisUtils.keys(pattern + ":*");
    }

    /**
     * 实现命令：DEL key，删除一个key
     * realKey = prefix + key
     */
    public void del(KeyPrefix prefix, String key) {
        redisUtils.del(getRealKey(prefix, key));
    }

    public void del(String realKey) {
        redisUtils.del(realKey);
    }

    /**
     * 实现命令：SET key value，设置一个key-value（将字符串值 value关联到 key）
     * realKey = prefix + key
     */
    public void set(KeyPrefix prefix, String key, String value) {
        if(prefix.expireSeconds() <= 0){
            redisUtils.set(getRealKey(prefix, key), value);
        }else{
            redisUtils.set(getRealKey(prefix, key), value, prefix.expireSeconds());
        }
    }

    public <T> void set(KeyPrefix prefix, String key, T value) {
        if(prefix.expireSeconds() <= 0){
            redisUtils.set(getRealKey(prefix, key), FastjsonUtils.convertObjectToJSON(value));
        }else{
            redisUtils.set(getRealKey(prefix, key), FastjsonUtils.convertObjectToJSON(value), prefix.expireSeconds());
        }
    }

    /**
     * 实现命令：SET key value EX seconds，设置key-value和超时时间（秒）
     * realKey = prefix + key
     */
    public void set(KeyPrefix prefix, String key, String value, long timeout) {
        redisUtils.set(getRealKey(prefix, key), value, timeout);
    }

    public <T> void set(KeyPrefix prefix, String key, T value, long timeout) {
        redisUtils.set(getRealKey(prefix, key), FastjsonUtils.convertObjectToJSON(value), timeout);
    }

    /**
     * 实现命令：SETNX key value，设置一个key-value（将字符串值 value关联到 key）
     * realKey = prefix + key
     */
    public Boolean setnx(KeyPrefix prefix, String key, String value){
        if(prefix.expireSeconds() <= 0){
            return redisUtils.setnx(getRealKey(prefix, key), value);
        }else{
            return redisUtils.setnx(getRealKey(prefix, key), value, prefix.expireSeconds());
        }
    }

    public <T> Boolean setnx(KeyPrefix prefix, String key, T value){
        if(prefix.expireSeconds() <= 0){
            return redisUtils.setnx(getRealKey(prefix, key), FastjsonUtils.convertObjectToJSON(value));
        }else{
            return redisUtils.setnx(getRealKey(prefix, key), FastjsonUtils.convertObjectToJSON(value), prefix.expireSeconds());
        }
    }

    /**
     * 实现命令：SETNX key value EX seconds，设置key-value和超时时间（秒）
     * realKey = prefix + key
     */
    public Boolean setnx(KeyPrefix prefix, String key, String value, long timeout) {
        return redisUtils.setnx(getRealKey(prefix, key), value, timeout);
    }

    public <T> Boolean setnx(KeyPrefix prefix, String key, T value, long timeout) {
        return redisUtils.setnx(getRealKey(prefix, key), FastjsonUtils.convertObjectToJSON(value), timeout);
    }

    /**
     * 实现命令：GET key，返回 key所关联的字符串值。
     * realKey = prefix + key
     */
    public String get(KeyPrefix prefix, String key) {
        return redisUtils.get(getRealKey(prefix, key));
    }

    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {
        return redisUtils.get(getRealKey(prefix, key), clazz);
    }

    /**
     * 实现命令：GET key，返回 key所关联的字符串值。
     * realKey = prefix + key
     */
    public Boolean exists(KeyPrefix prefix, String key) {
        return  redisUtils.exists(getRealKey(prefix, key));
    }

}
