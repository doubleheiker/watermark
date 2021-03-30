package com.watermark.main.controller;

import com.watermark.main.entity.LogInfo;
import com.watermark.main.service.LogInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/admin")
public class sysController {
    final
    LogInfoService logInfoService;

    public sysController(LogInfoService logInfoService) {
        this.logInfoService = logInfoService;
    }

    /**
     * 用户查询.
     * @return
     */
    @RequestMapping("/userInfo")
    @RequiresPermissions("userInfo:view")//权限管理;
    public String userInfo(){
        return "/admin/userInfo";
    }

    /**
     * 用户添加;
     * @return
     */
    @RequestMapping("/userInfoAdd")
    @RequiresPermissions("userInfo:add")//权限管理;
    public String userInfoAdd(){
        return "/admin/userInfoAdd";
    }

    /**
     * 用户删除;
     * @return
     */
    @RequestMapping("/userInfoDel")
    @RequiresPermissions("userInfo:del")//权限管理;
    public String userDel(){
        return "/admin/userInfoDel";
    }

    /**
     * 日志;
     * @return /admin/log
     */
    @RequestMapping("/logAdmin")
    public String logAdmin(ModelMap mp, @RequestParam(value = "pageNum", defaultValue = "0") int pageNum, @RequestParam(value = "pageSize", defaultValue = "12") int pageSize){
        Page<LogInfo> logInfoList = logInfoService.getLogList(pageNum, pageSize);
        mp.addAttribute("logList", logInfoList);
        return "/admin/log";
    }

    /**
     * 水印密钥管理-由用户删除不想存储的密钥.
     * @return /admin/keymanager
     */
    @RequestMapping("/delete")
    public String deleteKey(Long id) {
        logInfoService.save("删除单项日志记录",1,0);
        logInfoService.delete(id);
        return "redirect:/admin/logAdmin";
    }

    //按操作者查询，按操作状态查询
    @RequestMapping("/search")
    public String searchByUser(ModelMap mp, HttpServletRequest request, @RequestParam(value = "pageNum", defaultValue = "0") int pageNum, @RequestParam(value = "pageSize", defaultValue = "12") int pageSize) throws ParseException {
        String name = request.getParameter("name");
        String mode = request.getParameter("searchMode");
        System.out.println(mode);
        switch (mode) {
            case "searchByUser":
                Page<LogInfo> logInfoList = logInfoService.searchByUsername(name, pageNum, pageSize);
                //todo:判断是否存在该用户名
                mp.addAttribute("logList", logInfoList);
                break;
            case "searchByStatus":
                logInfoList = logInfoService.searchByStatus(Integer.valueOf(name), pageNum, pageSize);
                mp.addAttribute("logList", logInfoList);
                break;
            case "searchByDate":
                logInfoList = logInfoService.searchByDate(name, pageNum, pageSize);
                mp.addAttribute("logList", logInfoList);
                break;
            default:
                logInfoList = logInfoService.getLogList(pageNum, pageSize);
                mp.addAttribute("logList", logInfoList);
                break;
        }

        logInfoService.save("日志查询",1,2);
        return "/admin/log";
    }
}
