package com.watermark.main.service;

import com.watermark.main.entity.UserInfo;
import com.watermark.main.repository.UserInfoRepository;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

    @Override
    public UserInfo register(String username, String password) {
        UserInfo userInfo=new UserInfo();
        //userInfo.setUid(uid);
        userInfo.setUsername(username);

        String rawSalt = UUID.randomUUID().toString();//初始盐
        userInfo.setSalt(rawSalt);
        String algorithmName = "MD5";
        //盐值，用于和密码混合起来用
        ByteSource salt = ByteSource.Util.bytes(username+rawSalt);
        //加密次数
        int hashIterations = 1;
        //通过SimpleHash对密码进行哈希操作
        SimpleHash hash = new SimpleHash(algorithmName, password, salt, hashIterations);
        userInfo.setPassword(hash.toString());

        //验证该用户名是否被使用
        UserInfo user = findByUsername(username);
        if(user == null) {
            return userInfoRepository.save(userInfo);
        } else {
            return null;
        }
    }

    @Override
    public int deleteByUid(String username) {
        userInfoRepository.deleteByUsername(username);
        return 1;
    }
}
