package com.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * Created By Cx On 2018/10/15 12:04
 */
@Component
public class JedisUtil {

    @Autowired
    JedisPool jedisPool;

    //单例模式
    private Jedis getResource(){
        return jedisPool.getResource();
    }

    public byte[] set(byte[] key, byte[] value) {
        //获取Jedis,防止异常导致jedis未关闭，该功能为JDK7以后新增的ResourceManager
        try (Jedis jedis = getResource()) {
            jedis.set(key, value);
        }
        return value;
    }

    public void expire(byte[] key, int seconds) {
        try(Jedis jedis = getResource()){
            jedis.expire(key,seconds);
        }
    }

    public byte[] get(byte[] key) {
        try(Jedis jedis = getResource()){
            return jedis.get(key);
        }
    }

    public void del(byte[] key) {
        try(Jedis jedis = getResource()){
            jedis.del(key);
        }
    }

    //获取所有和pattern*匹配的key
    public Set<byte[]> keys(String pattern) {
        try(Jedis jedis = getResource()){
            return jedis.keys(pattern.concat("*").getBytes());
        }
    }
}
