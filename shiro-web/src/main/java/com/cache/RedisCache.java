package com.cache;

import com.util.JedisUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Set;

/**
 * Created By Cx On 2018/10/15 18:27
 */
@Component
public class RedisCache<K,V> implements Cache<K,V> {

    @Resource
    JedisUtil jedisUtil;

    private final String REDIS_CACHE_PREFIX = "cache:";

    private byte[] getKey(K k){
        if (k instanceof String){
            //如果key是String类型
            return (REDIS_CACHE_PREFIX + k).getBytes();
        }
        return SerializationUtils.serialize(k);
    }

    @Override
    public V get(K k) throws CacheException {
        System.out.println("从cache中获取数据");
        byte[] value = jedisUtil.get(getKey(k));
        //将byte数组反序列化为对象
        return (V) SerializationUtils.deserialize(value);
    }

    @Override
    public V put(K k, V v) throws CacheException {
        byte[] key = getKey(k);
        //将对象序列化为一个byte数组
        byte[] value = SerializationUtils.serialize(v);
        jedisUtil.set(key,value);
        //设置过期时间，单位s
        jedisUtil.expire(key,600);
        return v;
    }

    @Override
    public V remove(K k) throws CacheException {
        byte[] key = getKey(k);
        V v = (V) SerializationUtils.deserialize(jedisUtil.get(key));
        jedisUtil.del(key);
        return v;
    }

    @Override
    public void clear() throws CacheException {
        //切忌不可直接清空redis，因为redis中可能还存有其他数据，所以只能清空cache:*的K-V
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Set<K> keys() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }
}
