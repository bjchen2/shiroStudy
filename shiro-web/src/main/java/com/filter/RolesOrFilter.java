package com.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 实现一个Shiro Filter，filter实现只要用户拥有参数中的任一角色就能访问
 * Created By Cx On 2018/10/14 22:53
 */
public class RolesOrFilter extends AuthorizationFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        //获取用户主体
        Subject subject = getSubject(servletRequest,servletResponse);
        //获取参数
        String[] roles = (String[]) o;
        if (roles == null || roles.length == 0) return true;
        for (String role : roles){
            //一旦发现用户拥有某个role，直接返回true
            if (subject.hasRole(role)) return true;
        }
        return false;
    }
}
