package com.watermark.main.configuration;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfiguration {
    @Bean
    public ShiroFilterFactoryBean ShiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //TODO:完善过滤器
        //添加shiro内置过滤器完成拦截
        Map<String, String> filterMap = new LinkedHashMap<>();

        //授权,正常情况下，未授权会跳到非授权页面
        //filterMap.put("/user/add", "perms[user:add]");
        //filterMap.put("/user/update", "perms[user:update]");
        //配置退出过滤器，具体的推出代码shiro已经实现
        filterMap.put("/logout", "logout");

        //<!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
        //<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        filterMap.put("/user/*", "authc,roles[user]");
        filterMap.put("/admin/*", "authc,roles[admin]");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);

        //设置登录的请求
        shiroFilterFactoryBean.setLoginUrl("/toLogin");
        //设置非授权页面请求
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");


        return shiroFilterFactoryBean;
    }

    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm());

        return securityManager;
    }

    @Bean
    public MyShiroRealm myShiroRealm() {
        //注入realm
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        //注入密码加密
        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());

        return myShiroRealm;
    }

    //密码加密算法设置
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //设置加密方式
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        //设置散列的次数
        hashedCredentialsMatcher.setHashIterations(1);
        return hashedCredentialsMatcher;
    }

    //整合ShiroDialect：用于整合thymeleaf
    @Bean
    public ShiroDialect getShiroDialect() {
        return new ShiroDialect();
    }

}
