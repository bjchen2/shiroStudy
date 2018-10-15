package com.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;

import javax.servlet.ServletRequest;
import java.io.Serializable;

/**
 * 自定义session manager，因为自带的session manager有些功能过于繁琐，所以可以按需重写
 * 这样修改后，当访问接口时只会访问一次redis，而按默认方式会访问7-9次
 * Created By Cx On 2018/10/15 15:24
 */
public class CustomSessionManager extends DefaultWebSessionManager {
    //如果session中有sessionId对应的session对象，则直接从session中获取，不再访问redis，如果没有，先访问redis获取，再添加到request中
    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        //获取sessionId
        Serializable sessionId = getSessionId(sessionKey);
        ServletRequest request = null;
        if (sessionKey instanceof WebSessionKey){
            //如果sessionKey能够转换为WebSessionKey，则获取request
            request = ((WebSessionKey) sessionKey).getServletRequest();
        }
        if (request != null && sessionId != null){
            Session session = (Session) request.getAttribute(sessionId.toString());
            if (session != null) return session;
        }
        //如果没有获取到session对象，则先从redis中获取，再添加到request中
        Session session = super.retrieveSession(sessionKey);
        if (request != null && sessionId != null){
            request.setAttribute(sessionId.toString(),session);
        }
        return session;
    }
}
