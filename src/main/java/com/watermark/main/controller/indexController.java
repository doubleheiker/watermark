package com.watermark.main.controller;

import com.watermark.main.entity.UserInfo;
import com.watermark.main.repository.UserInfoRepository;
import com.watermark.main.service.UserInfoServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
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
    public String index() {
        return "index";
    }

    @GetMapping("/toSearch")
    public String toSearch() {
        return "search";
    }

    @GetMapping("/toDetect")
    public String toDetect() {
        return "detect";
    }

    @GetMapping("/toLogin")
    public String toLogin() {
        return "login";
    }
    @RequestMapping(value = "/login")
    public String login(String username, String password, Model model) {
        //获取当前用户
        Subject currentUser = SecurityUtils.getSubject();

        //用户是否已经登录，未登录则进行登录
        if (!currentUser.isAuthenticated()) {
            //封装用户输入的用户名和密码
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);

            try {
                //登录，进行密码比对，登录失败时将会抛出对应异常
                currentUser.login(usernamePasswordToken);
                return "index";
            } catch (UnknownAccountException uae) {
                model.addAttribute("msg", "用户名不存在");
                return "login";
            } catch (IncorrectCredentialsException ice) {
                model.addAttribute("msg", "密码错误");
                return "login";
            } catch (LockedAccountException lae) {
                model.addAttribute("msg", "用户状态异常");
                return "login";
            } catch (AuthenticationException ae) {
                model.addAttribute("msg", "登录失败，请与管理员联系");
                return "login";
            }
        } else {
            model.addAttribute("msg", "您已经登录了！");
            return "index";
        }
    }

    @GetMapping("/toRegister")
    public String toRegister() {
        return "register";
    }
    @RequestMapping(value = "/register")
    public String register(String username, String password, Model model){
        UserInfo registerUser = userInfoServiceImpl.register(username, password);
        if (registerUser == null) {
            model.addAttribute("msg", "该用户名已存在！");
            return "login";
        } else {
            model.addAttribute("msg", "注册成功！！");
            return "index";
        }
    }

    @GetMapping("/unauthorized")
    @ResponseBody
    public String unauthorizedUrl() {
        return "非授权访问！";
    }
}
