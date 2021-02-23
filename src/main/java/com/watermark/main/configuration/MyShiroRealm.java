package com.watermark.main.configuration;

import com.watermark.main.entity.UserInfo;
import com.watermark.main.service.UserInfoService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

public class MyShiroRealm extends AuthorizingRealm {

    @Autowired
    UserInfoService userInfoService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("执行了=>授权");

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //TODO：从数据库表中获取权限，首先需要拿到当前登录用户对象
        Subject subject = SecurityUtils.getSubject();
        //获取principal
        UserInfo currentUser = (UserInfo) subject.getPrincipal();

        if (currentUser.getPerms() == null) {
            info.addStringPermission("");
        }
        else info.addStringPermission(currentUser.getPerms());

        return info;
    }
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("执行了=>认证");

        UsernamePasswordToken userToken = (UsernamePasswordToken) token;
        //根据用户名查找到登录用户，并保存到UserInfo对象中
        UserInfo user = userInfoService.findByUsername(userToken.getUsername());
        //String username = "yk";
        //String password = "123456";

        if (user == null) {
            return null; //抛出 UnknownAccountException
        }
        //密码认证shiro自动完成，principal设置为user，这样授权函数中可以获取到设置的principal
        return new SimpleAuthenticationInfo(user,user.getPassword(),"");
    }
}
