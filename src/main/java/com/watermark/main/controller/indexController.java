package com.watermark.main.controller;

import com.watermark.main.DPWA.Dataset;
import com.watermark.main.entity.DataSource;
import com.watermark.main.entity.UserInfo;
import com.watermark.main.repository.DataSourceRepository;
import com.watermark.main.repository.UserInfoRepository;
import com.watermark.main.service.DataSourceService;
import com.watermark.main.service.UserInfoServiceImpl;
import com.watermark.main.utils.DownloadFile;
import com.watermark.main.utils.ReadFile;
import com.watermark.main.utils.wmoperate.JudgeUtils;
import com.watermark.main.utils.wmoperate.WaterMarking;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


@Controller
public class indexController {
    final
    UserInfoServiceImpl userInfoServiceImpl;
    final
    UserInfoRepository userInfoRepository;
    final
    DataSourceService dataSourceService;
    final
    DataSourceRepository dataSourceRepository;

    public indexController(UserInfoServiceImpl userInfoServiceImpl, UserInfoRepository userInfoRepository, DataSourceService dataSourceService, DataSourceRepository dataSourceRepository) {
        this.userInfoServiceImpl = userInfoServiceImpl;
        this.userInfoRepository = userInfoRepository;
        this.dataSourceService = dataSourceService;
        this.dataSourceRepository = dataSourceRepository;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

//    @GetMapping("/toSearch")
//    public String toSearch() {
//        return "search";
//    }
    @RequestMapping("/toSearch")
    public String toSearch(ModelMap mp, HttpServletRequest request, @RequestParam(value = "pageNum", defaultValue = "0") int pageNum, @RequestParam(value = "pageSize", defaultValue = "4") int pageSize) {
        if (request.getParameter("name") == null) {
            Page<DataSource> fileList = dataSourceService.getFileList(pageNum, pageSize);
            mp.addAttribute("fileList", fileList);
            return "search";
        } else {
            String name = request.getParameter("name");
            Page<DataSource> res = dataSourceService.findByOriginFileName(name, pageNum, pageSize);
            mp.addAttribute("fileList", res);
            return "search";
        }
    }
//    @PostMapping("/search")
//    public String search(ModelMap mp, HttpServletRequest request, @RequestParam(value = "pageNum", defaultValue = "0") int pageNum, @RequestParam(value = "pageSize", defaultValue = "4") int pageSize) {
//        String name = request.getParameter("name");
//        Page<DataSource> res = dataSourceService.findByOriginFileName(name, pageNum, pageSize);
//        mp.addAttribute("search_result", res);
//        return "redirect:search";
//    }

    @RequestMapping("/fileDownload")
    @ResponseBody
    public String fileDownload(Long id) throws UnsupportedEncodingException {
        String filepath = dataSourceRepository.findByFid(id).getUrl();
        String filename = dataSourceRepository.findByFid(id).getHashName();
        File file = new File(filepath);

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return "无效请求！";
        }
        HttpServletResponse response = requestAttributes.getResponse();
        if (response == null) {
            return "无效请求！";
        }

        response.setContentType("text/plain;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        // response.setContentType("application/force-download");
        response.setHeader("Content-Disposition", "attachment;fileName=" +   java.net.URLEncoder.encode(filename,"UTF-8"));
        DownloadFile.download(response, file);

        return null;
    }
    @RequestMapping("/checkFileDownload")
    @ResponseBody
    public String checkFileDownload(Long id) throws UnsupportedEncodingException {
        String filename = "checkFile";
        String Key = dataSourceRepository.findByFid(id).getK();
        Double M = dataSourceRepository.findByFid(id).getM();
        Integer L = dataSourceRepository.findByFid(id).getL();
        String checkStr = "KEY: " + Key + " M: " + M + " L: " + L;

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return "无效请求！";
        }
        HttpServletResponse response = requestAttributes.getResponse();
        if (response == null) {
            return "无效请求！";
        }

        response.setContentType("text/plain;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        // response.setContentType("application/force-download");
        response.setHeader("Content-Disposition", "attachment;fileName=" +   java.net.URLEncoder.encode(filename,"UTF-8"));

        //从数据库中读取K M L
        DownloadFile.downloadCheckFile(response, checkStr);
        return null;
    }

    @GetMapping("/toDetect")
    public String toDetect() {
        return "detect";
    }
    @PostMapping("/detect")
    public String detect(@RequestParam("file") MultipartFile file, @RequestParam("key") String K,
                         @RequestParam("markedline") String ml, @RequestParam("M") String m, ModelMap mp) throws Exception {
        //判断文件是否为空
        if(file.isEmpty()){
            mp.addAttribute("result_file", "上传失败，请选择合适的文件上传！");
            return "/detect";
        }
        File toFile = ReadFile.multipartFileToFile(file);
        if (ml == null) {
            mp.addAttribute("result_file", "上传失败，请输入正确的参数L！！");
            return "/detect";
        }

        try {
            //转换MutipartFile类型为File类型
            Dataset dataset;
            //读取并处理上传的文件为dataset类型
            dataset = ReadFile.readTable(toFile);

            //调用检测API
            WaterMarking waterMark = new WaterMarking();
            Double M = Double.parseDouble(m);
            Integer markedLine = Integer.parseInt(ml) - 1;

            //判断markedLine是否超过最大列，以及所选列是否是数值型
            JudgeUtils judgeUtils = new JudgeUtils();
            judgeUtils.JudgeMarkedLine(dataset, markedLine);

            boolean detectResult = waterMark.Detect(dataset, K, M, markedLine);
            String res;

            if (detectResult) {
                res = "检测到水印！";
            } else {
                res = "未检测到水印！";
            }

            mp.addAttribute("result_file", "上传成功");
            mp.addAttribute("detectResult", res);
        } catch (Exception e) {
            e.printStackTrace();
            //出现异常，则告诉页面失败
            mp.addAttribute("result_file", "上传失败！请检查文件是否存在或者检测参数是否正确！");
        } finally {
            // 会在本地产生临时文件，用完后需要删除
            if (toFile.exists()) {
                toFile.delete();
            }
        }

        return "/detect";
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
                return "redirect:/";
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
