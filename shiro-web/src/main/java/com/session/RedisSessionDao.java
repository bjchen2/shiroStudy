package com.session;

import com.util.JedisUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.util.CollectionUtils;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 重写session增删改查操作
 * Created By Cx On 2018/10/15 12:03
 */
public class RedisSessionDao extends AbstractSessionDAO {

    @Resource
    JedisUtil jedisUtil;

    private String REDIS_SESSION_PREFIX = "session:";

    //将key转为二进制数组存储
    private byte[] getKey(String key){
        return REDIS_SESSION_PREFIX.concat(key).getBytes();
    }

    private void saveSession(Session session){
        if(session != null && session.getId() != null){
            byte[] key = getKey(session.getId().toString());
            //将session对象序列化为一个byte数组
            byte[] value = SerializationUtils.serialize(session);
            jedisUtil.set(key,value);
            //设置过期时间，单位s
            jedisUtil.expire(key,600);
        }
    }


    @Override
    protected Serializable doCreate(Session session) {
        //注意，不能用session.getId(),此时id还为空，需要调用generateSessionId方法生成并返回
        Serializable sessionId = generateSessionId(session);
        //将session与sessionId捆绑，相当于setSessionId
        assignSessionId(session,sessionId);
        saveSession(session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        System.out.println("===========ddd===");
        if (sessionId == null) return null;
        byte[] key = getKey(sessionId.toString());
        byte[] value = jedisUtil.get(key);
        //将byte数组反序列化为Session对象
        return (Session) SerializationUtils.deserialize(value);
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        saveSession(session);
    }

    @Override
    public void delete(Session session) {
        if(session != null && session.getId() != null){
            jedisUtil.del(getKey(session.getId().toString()));
        }
    }

    @Override
    public Collection<Session> getActiveSessions() {
        //获取jedis中的所有key
        Set<byte[]> keys = jedisUtil.keys(REDIS_SESSION_PREFIX);
        Set<Session> sessions = new HashSet<>();
        if (CollectionUtils.isEmpty(keys)) return sessions;
        for (byte[] key : keys){
            Session session = (Session) SerializationUtils.deserialize(jedisUtil.get(key));
            sessions.add(session);
        }
        return sessions;
    }
}
