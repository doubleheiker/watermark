package com.watermark.main.service;

import com.watermark.main.entity.UserInfo;
import com.watermark.main.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService{
    private final UserInfoRepository userInfoRepository;

    public UserInfoServiceImpl(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    public UserInfo findByUsername(String username) {
        System.out.println("UserInfoServiceImpl.findByUsername");

        return userInfoRepository.findByUsername(username);
    }
    /*public Page<UserInfo> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<UserInfo> UserInfos=userInfoRepository.findAll(pageable);

        return UserInfos;
    }
    public int count() {
        return this.findAll(0, 100000).getSize();
    }*/
    public void save(String username, String password) {
        UserInfo userInfo=new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPassword(password);
        userInfoRepository.save(userInfo);
    }
}
