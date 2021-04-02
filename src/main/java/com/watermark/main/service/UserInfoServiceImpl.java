package com.watermark.main.service;

import com.watermark.main.entity.SysRole;
import com.watermark.main.entity.UserInfo;
import com.watermark.main.repository.SysRoleRepository;
import com.watermark.main.repository.UserInfoRepository;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
public class UserInfoServiceImpl implements UserInfoService{
    private final UserInfoRepository userInfoRepository;
    private final SysRoleRepository sysRoleRepository;

    public UserInfoServiceImpl(UserInfoRepository userInfoRepository, SysRoleRepository sysRoleRepository) {
        this.userInfoRepository = userInfoRepository;
        this.sysRoleRepository = sysRoleRepository;
    }

    @Override
    public UserInfo findByUsername(String username) {
        System.out.println("UserInfoServiceImpl.findByUsername");
        return userInfoRepository.findByUsername(username);
    }

    @Override
    public Page<UserInfo> findAll(int pageNum, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.ASC, "username");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        return userInfoRepository.findAll(pageable);
    }

    //只用于用户注册
    @Override
    public UserInfo register(String username, String password) {
        UserInfo userInfo=new UserInfo();

        //添加用户角色
        SysRole sysRole = sysRoleRepository.findByRole("user");
        List<SysRole> sysRoles = new LinkedList<>();
        sysRoles.add(sysRole);

        //userInfo.setUid(uid);
        userInfo.setUsername(username);
        userInfo.setRoleList(sysRoles);

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

    //只用于用户注册
    @Override
    public UserInfo addAdmin(String username, String password) {
        UserInfo userInfo=new UserInfo();

        //添加用户角色
        SysRole sysRole = sysRoleRepository.findByRole("admin");
        List<SysRole> sysRoles = new LinkedList<>();
        sysRoles.add(sysRole);

        //userInfo.setUid(uid);
        userInfo.setUsername(username);
        userInfo.setRoleList(sysRoles);

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

    /**
     * 重置密码
     * @return 1
     */
    @Override
    public int resetPassword(String username, String newPassword) {
        UserInfo user = userInfoRepository.findByUsername(username);
        String algorithmName="MD5";
        ByteSource salt = ByteSource.Util.bytes(username+user.getSalt());
        int hashIterations = 1;
        SimpleHash hash = new SimpleHash(algorithmName, newPassword, salt, hashIterations);
        user.setPassword(hash.toString());
        user.setState("1");
        userInfoRepository.save(user);
        return 1;
    }

    @Override
    public int deleteByUsername(String username) {
        userInfoRepository.deleteByUsername(username);
        return 1;
    }
}
