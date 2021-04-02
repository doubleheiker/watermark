package com.watermark.main.controller;

import com.alibaba.fastjson.JSONObject;
import com.watermark.main.entity.UserInfo;
import com.watermark.main.service.LogInfoService;
import com.watermark.main.service.UserInfoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/net")
public class netAjaxController {
    final
    LogInfoService logInfoService;
    final
    UserInfoService userInfoService;

    public netAjaxController(LogInfoService logInfoService, UserInfoService userInfoService) {
        this.logInfoService = logInfoService;
        this.userInfoService = userInfoService;
    }

    @RequestMapping("/getUser")
    public String getUser(@RequestParam(name = "username", required = false, defaultValue = "0") String username) {
        UserInfo userInfo= userInfoService.findByUsername(username);
        JSONObject jsonObject = new JSONObject();
        if (userInfo!=null) {
            jsonObject.put("content",userInfo);
            jsonObject.put("code", 0);
        }else {
            jsonObject.put("code", -1);
        }
        return jsonObject.toJSONString();
    }

    @RequestMapping("/addAdmin")
    public String userInfoList(@RequestParam(name = "username", required = false, defaultValue = "0") String username,
                               @RequestParam(name = "password", required = false, defaultValue = "0") String password) {
        JSONObject jsonObject = new JSONObject();
        if(username.equals("0")||password.equals("0")||password.length()<8) {
            jsonObject.put("code", -1);
            String str=String.format("username:%s ",username);
            logInfoService.save("添加用户失败,参数为 "+str, 0, 0);
        }else {
            userInfoService.addAdmin(username, password);
            System.out.println("添加admin");
            jsonObject.put("code", 0);
            String str=String.format("username:%s ",username);
            logInfoService.save("添加用户成功,参数为 "+str, 1, 0);
        }
        return jsonObject.toJSONString();
    }

    @RequestMapping("/delUser")
    public String delUser(@RequestParam(name = "username", required = false, defaultValue = "0") String username) {
        JSONObject jsonObject = new JSONObject();
        if (username.equals("0")) {
            jsonObject.put("code", -1);
            String str=String.format("username:%s ",username);
            logInfoService.save("删除用户失败,参数为 "+str, 0, 0);
        }else {
            userInfoService.deleteByUsername(username);
            jsonObject.put("code", 0);
            String str=String.format("username:%s ",username);
            logInfoService.save("删除用户成功,参数为 "+str, 1, 0);
        }
        return jsonObject.toJSONString();
    }

    @RequestMapping("/resetstr")
    public String resetstr() {
        String spestr="!@#$%^&*.";
        String upStr="qwertyuiopasdfghjklzxcvbnm";
        String ditstr="12345677891230";

        int i1=(new Random().nextInt(10000)+10000)%7;
        int i2=(new Random().nextInt(10000)+10000)%24;
        int i3=(new Random().nextInt(10000)+10000)%12;
        System.out.println(i1+" "+i2);
        String tString=spestr.substring(i1, i1+1);
        tString=tString+upStr.substring(i2, i2+1);
        tString=tString+ditstr.substring(i3, i3+1);
        String ret=randomSeq()+tString+randomSeq()+randomSeq();
        return ret.substring(0, 10);
    }

    @RequestMapping("/resetUser")
    public String resetUser(@RequestParam(name = "username", required = false, defaultValue = "0") String username,
                            @RequestParam(name = "newpassword", required = false, defaultValue = "0") String newpassword) {
        JSONObject jsonObject = new JSONObject();
        userInfoService.resetPassword(username, newpassword);
        jsonObject.put("code", 0);
        String str=String.format("username:%s ",username);
        logInfoService.save("重置用户密码成功,参数为 "+str, 1, 0);
        System.out.println("重置");
        return jsonObject.toJSONString();
    }

    @RequestMapping("/jumping")
    public String jumping() {
        return "1";
    };

    private String randomSeq() {
        String visibleChars = "ABCDEFGHIJKLMNOPQRSTUVWXYabcdefghijklmnopqrstuvwxy1234567890";
        Random r = new Random();

        int i1 = r.nextInt(10000) % 59;
        int i2 = r.nextInt(10000) % 59;
        int i3 = r.nextInt(10000) % 59;
        String retVersion = visibleChars.substring(i1, i1 + 1) + visibleChars.substring(i2, i2 + 1)
                + visibleChars.substring(i3, i3 + 1);
        return retVersion;
    }

}
