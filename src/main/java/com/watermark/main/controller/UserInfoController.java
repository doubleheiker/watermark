package com.watermark.main.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserInfoController {
    /**
     * 用户查询.
     * @return
     */
    @RequestMapping("/userInfo")
    @RequiresPermissions("userInfo:view")//权限管理;
    public String userInfo(){
        return "user/userInfo";
    }

    /**
     * 用户添加;
     * @return
     */
    @RequestMapping("/userInfoAdd")
    @RequiresPermissions("userInfo:add")//权限管理;
    public String userInfoAdd(){
        return "user/userInfoAdd";
    }

    /**
     * 用户删除;
     * @return
     */
    @RequestMapping("/userInfoDel")
    @RequiresPermissions("userInfo:del")//权限管理;
    public String userDel(){
        return "user/userInfoDel";
    }
}
