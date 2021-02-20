package com.watermark.main.controller;

import com.watermark.main.entity.UserInfo;
import com.watermark.main.repository.UserInfoRepository;
import com.watermark.main.service.UserInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
        return "hello";
    }

    @GetMapping("/save")
    public UserInfo crudRepository_save() {

        // 保存一个用户michael
        UserInfo userInfo=new UserInfo();
        userInfo.setUsername("yk");
        userInfo.setPassword("123456");
        UserInfo res = userInfoRepository.save(userInfo);
        return res;
    }
}
