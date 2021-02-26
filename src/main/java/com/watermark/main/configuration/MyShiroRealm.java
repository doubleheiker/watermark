package com.watermark.main.configuration;

import com.watermark.main.entity.SysPermission;
import com.watermark.main.entity.SysRole;
import com.watermark.main.entity.UserInfo;
import com.watermark.main.service.UserInfoService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

public class MyShiroRealm extends AuthorizingRealm {

    @Autowired
    UserInfoService userInfoService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("执行了=>授权");

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        UserInfo currentUser = (UserInfo) principals.getPrimaryPrincipal();

        for (SysRole role:currentUser.getRoleList()) {
            info.addRole(role.getRole());
            for (SysPermission perms:role.getPermissons()) {
                info.addStringPermission(perms.getPermission());
            }
        }

        return info;
    }
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("执行了=>认证");

        UsernamePasswordToken userToken = (UsernamePasswordToken) token;
        //根据用户名查找到登录用户，并保存到UserInfo对象中
        UserInfo user = userInfoService.findByUsername(userToken.getUsername());

        if (user == null) {
            return null; //抛出 UnknownAccountException
        }

        //session
        Subject currentSubject = SecurityUtils.getSubject();
        Session session = currentSubject.getSession();
        session.setAttribute("loginUser", user);

        //密码认证shiro自动完成，principal设置为user，这样授权函数中可以获取到设置的principal
        return new SimpleAuthenticationInfo(user,user.getPassword(), ByteSource.Util.bytes(user.getCredentialsSalt()),getName());
    }
}
