package com.shiro.realm;

import com.dao.UserDao;
import com.vo.UserVO;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created By Cx On 2018/9/20 20:16
 */
public class CustomRealm extends AuthorizingRealm {

    //@Resource默认使用名称匹配，@Autowired默认使用类型匹配,比如这个变量名叫uu就会匹配id为uu的bean
    @Resource
    private UserDao userDao;

    //书写用户授权逻辑（即判断用户是否有权限做什么）
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String) principalCollection.getPrimaryPrincipal();
        //从数据库中获取username对应的用户角色
        Set<String> roles = getRolesByUsername(username);
        //从数据库中获取username对应的用户权限
        Set<String> permissions = getPermissionsByRoles(roles);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo(roles);
        authorizationInfo.setStringPermissions(permissions);
        return authorizationInfo;
    }

    private Set<String> getPermissionsByRoles(Set<String> roles) {
        System.out.println("从数据库中获取用户权限信息数据");
        //模拟数据库操作，获取某个用户对应的所有权限并返回（相当于获取该用户所有角色的权限）
        Set<String> permissions = new HashSet<>();
        permissions.add("angry");
        permissions.add("red");
        return permissions;
    }

    private Set<String> getRolesByUsername(String username) {
        System.out.println("从数据库中获取用户角色信息数据");
        //模拟数据库操作，获取username对应的用户角色并返回
        Set<String> roles = new HashSet<>();
        roles.add("admin");
        roles.add("boy");
        return roles;
    }

    // 书写用户认证逻辑（即判断用户是否可以登录）
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();
        //从数据库中获取username对应的password并返回
        String password = getPasswordByUsername(username);
        if (password == null) return null;
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username,password, this.getName());
        //设置md5加密盐值
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("hello"));
        return authenticationInfo;
    }

    private String getPasswordByUsername(String username) {
        System.out.println("从数据库中获取用户信息数据");
        UserVO userVO = userDao.getUserByUserName(username);
        if (userVO == null) return null;
        return userVO.getPassword();
    }

    public static void main(String[] args) {
        System.out.println(new Md5Hash("admin","hello"));
    }
}
