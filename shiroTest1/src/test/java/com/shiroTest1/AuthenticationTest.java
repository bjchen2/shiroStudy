package com.shiroTest1;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

/**
 * Authentication : 认证
 * Created By Cx On 2018/9/20 14:03
 */

public class AuthenticationTest {


    @Test
    public void testByIniRealm(){
        IniRealm iniRealm = new IniRealm("classpath:user.ini");
        //1、构建SecurityManager环境
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.setRealm(iniRealm);

        //2、主体提交认证请求
        //设置 SecurityManager 环境
        SecurityUtils.setSecurityManager(securityManager);
        //获取主体,注意，Subject 是 shiro 包下的
        Subject subject = SecurityUtils.getSubject();
        //创建一个认证数据
        UsernamePasswordToken token = new UsernamePasswordToken("admin","admin");
        //进行登录认证
        subject.login(token);
        //输出验证结果：验证通过会返回 true，未验证或退出验证后会返回 ： false
        // 验证失败会抛出异常 , 账号不存在为：UnknownAccountException，密码错误为：IncorrectCredentialsException
        System.out.println("认证结果：" + subject.isAuthenticated());
        //校验用户权限,该方法没有返回值且必须在认证成功后执行，如果校验失败，会抛出 UnauthorizedException 异常
        subject.checkRoles("red","man");
        subject.checkPermission("angry");
        //退出验证
        subject.logout();
    }

    @Test
    public void testBySimpleAccountRealm(){
        SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();
        //前两个参数是账号、密码，后面的多个参数均为角色
        simpleAccountRealm.addAccount("admin","admin","man","red");
        //1、构建SecurityManager环境
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.setRealm(simpleAccountRealm);

        //2、主体提交认证请求
        //设置 SecurityManager 环境
        SecurityUtils.setSecurityManager(securityManager);
        //获取主体,注意，Subject 是 shiro 包下的
        Subject subject = SecurityUtils.getSubject();
        //创建一个认证数据
        UsernamePasswordToken token = new UsernamePasswordToken("admin","admin");
        //进行登录认证
        subject.login(token);
        //输出验证结果：验证通过会返回 true，未验证或退出验证后会返回 ： false
        // 验证失败会抛出异常 , 账号不存在为：UnknownAccountException，密码错误为：IncorrectCredentialsException
        System.out.println("认证结果：" + subject.isAuthenticated());
        //校验用户权限,该方法没有返回值且必须在认证成功后执行，如果校验失败，会抛出 UnauthorizedException 异常
        subject.checkRoles("red","man");
        //退出验证
        subject.logout();

    }

    @Test
    public void testByJdbcRealm(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/cx_practice");
        dataSource.setUsername("root");
        dataSource.setPassword("111111");
        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(dataSource);
        //设置为可查询权限，默认为false，则只能查询角色，不能查询某个角色的权限
        jdbcRealm.setPermissionsLookupEnabled(true);
        jdbcRealm.setAuthenticationQuery("select password from user where username = ?");
        //1、构建SecurityManager环境
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.setRealm(jdbcRealm);

        //2、主体提交认证请求
        //设置 SecurityManager 环境
        SecurityUtils.setSecurityManager(securityManager);
        //获取主体,注意，Subject 是 shiro 包下的
        Subject subject = SecurityUtils.getSubject();
        //创建一个认证数据
        UsernamePasswordToken token = new UsernamePasswordToken("admin","admin");
        //进行登录认证
        subject.login(token);
        //输出验证结果：验证通过会返回 true，未验证或退出验证后会返回 ： false
        // 验证失败会抛出异常 , 账号不存在为：UnknownAccountException，密码错误为：IncorrectCredentialsException
        System.out.println("认证结果：" + subject.isAuthenticated());
        //校验用户权限,该方法没有返回值且必须在认证成功后执行，如果校验失败，会抛出 UnauthorizedException 异常
        subject.checkRoles("red","man");
        subject.checkPermission("angry");
        //退出验证
        subject.logout();
    }

    @Test
    public void testByCustomRealm(){
        CustomRealm customRealm = new CustomRealm();
        //1、构建SecurityManager环境
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.setRealm(customRealm);

        //构建加密策略
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        //设置加密方法
        matcher.setHashAlgorithmName("md5");
        //设置加密次数为一次
        matcher.setHashIterations(1);
        customRealm.setCredentialsMatcher(matcher);

        //2、主体提交认证请求
        //设置 SecurityManager 环境
        SecurityUtils.setSecurityManager(securityManager);
        //获取主体,注意，Subject 是 shiro 包下的
        Subject subject = SecurityUtils.getSubject();
        //创建一个认证数据
        UsernamePasswordToken token = new UsernamePasswordToken("admin","admin");
        //进行登录认证
        subject.login(token);
        //输出验证结果：验证通过会返回 true，未验证或退出验证后会返回 ： false
        // 验证失败会抛出异常 , 账号不存在为：UnknownAccountException，密码错误为：IncorrectCredentialsException
        System.out.println("认证结果：" + subject.isAuthenticated());
        //校验用户权限,该方法没有返回值且必须在认证成功后执行，如果校验失败，会抛出 UnauthorizedException 异常
        subject.checkRoles("red","man");
        subject.checkPermission("angry");
        //退出验证
        subject.logout();
    }
}
