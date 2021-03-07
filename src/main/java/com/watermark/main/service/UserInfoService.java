package com.watermark.main.service;

import com.watermark.main.entity.UserInfo;
import org.springframework.data.domain.Page;

public interface UserInfoService {
    UserInfo findByUsername(String username);
    //Page<UserInfo> findAll(int page, int size);
    UserInfo register(String username, String password);
    //int resetPassword(String username, String newpassword);

    //int count();
    int deleteByUid(String username);
    //int updatePassword(String username, String oldpassword, String newpassword);
    //TODO:用户角色、权限控制接口、修改密码

}