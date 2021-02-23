package com.watermark.main.controller;

import com.watermark.main.entity.UserInfo;
import com.watermark.main.repository.UserInfoRepository;
import com.watermark.main.service.UserInfoServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
public class indexController {
    final
    UserInfoServiceImpl userInfoServiceImpl;
    final
    UserInfoRepository userInfoRepository;

    public indexController(UserInfoServiceImpl userInfoServiceImpl, UserInfoRepository userInfoRepository) {
        this.userInfoServiceImpl = userInfoServiceImpl;
        this.userInfoRepository = userInfoRepository;
    }

    @GetMapping("/")
    public String hello() {
        return "index";
    }

    @GetMapping("/user/add")
    public String add() {
        return "user/add";
    }

    @GetMapping("/user/update")
    public String update() {
        return "user/update";
    }

    @GetMapping("/toLogin")
    public String toLogin() {
        return "login";
    }
    @PostMapping(value = "/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        //获取用户
        Subject subject = SecurityUtils.getSubject();
        //封装用户的登录数据
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
        //登录
        try {
            subject.login(usernamePasswordToken);
            model.addAttribute("username", username);
            return "index";
        } catch (UnknownAccountException e) {
            model.addAttribute("msg", "用户名错误！");
            return "login";
        } catch (IncorrectCredentialsException e) {
            model.addAttribute("msg", "密码错误！");
            return "login";
        }
        //UserInfo user = userInfoServiceImpl.findByUsername(username);
        //map.addAttribute("username", user.getUsername());
    }

    @GetMapping("/unauthorized")
    @ResponseBody
    public String unauthorizedUrl() {
        return "非授权访问！";
    }
}
