package com.watermark.main;

import com.watermark.main.service.UserInfoService;
import com.watermark.main.service.UserInfoServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MainApplicationTests {

    @Autowired
    UserInfoService userInfoService;

    @Test
    void contextLoads() {
        System.out.println(userInfoService.findByUsername("yk"));
    }

}
