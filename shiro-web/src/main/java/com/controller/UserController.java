package com.controller;

import com.vo.UserVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created By Cx On 2018/10/6 16:14
 */
@RestController
public class UserController {

    //注意：要在spring.xml中配置不用shiro验证，不然无法访问
    @PostMapping(value = "/subLogin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String subLogin(UserVO userVO){
        //获取主体,注意，Subject 是 shiro 包下的
        Subject subject = SecurityUtils.getSubject();
        //创建一个认证数据
        UsernamePasswordToken token = new UsernamePasswordToken(userVO.getUsername(),userVO.getPassword());
        //允许用户自动登录，自动登录的具体配置见spring.xml中的CookieRememberMeManager bean
        token.setRememberMe(userVO.getRememberMe()==null?false:userVO.getRememberMe());
        //进行登录认证
       try {
           //如果登录失败，会抛出异常
           subject.login(token);
       }catch (Exception e){
           return "登录失败";
       }
        if (subject.isAuthenticated()){
            return "登录成功";
        }
        return "登录失败";
    }

    //用户必须是 admin 的同时也是 boy 角色才能访问该链接
    @RequiresRoles(value = {"admin","boy"},logical = Logical.OR)
    //用户必须有red 的同时也是 angry权限才能访问该链接
    @RequiresPermissions({"red","angry"})
    @GetMapping("/test")
    public String testRoleByAnno(){
        return "test success";
    }

    //通过spring.xml调用自定义filter进行权限控制
    @GetMapping("/testByXml")
    public String testByXml(){
        return "test by xml successfully";
    }
}
