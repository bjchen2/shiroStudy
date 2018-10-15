package com.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

import javax.annotation.Resource;

/**
 * 通过使用cache，使服务器能够存缓存中直接获取用户认证数据（会先从缓存中取数据，没有取到才会访问数据库）
 * 但需要注意的是，数据库信息更新时需要将cache设为过期
 * todo 该案例并没有考虑这种情况
 * Created By Cx On 2018/10/15 18:27
 */
public class RedisCacheManager implements CacheManager {

    @Resource
    RedisCache redisCache;

    //参数s为cacheName，可以按需求将其与redisCache进行缓存
    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        return redisCache;
    }
}
