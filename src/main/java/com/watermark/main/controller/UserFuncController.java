package com.watermark.main.controller;

import com.watermark.main.DPWA.Dataset;
import com.watermark.main.entity.DataSource;
import com.watermark.main.entity.UserInfo;
import com.watermark.main.entity.WaterMarkKey;
import com.watermark.main.repository.DataSourceRepository;
import com.watermark.main.repository.WaterMarkKeyRepository;
import com.watermark.main.service.LogInfoService;
import com.watermark.main.service.WaterMarkKeyService;
import com.watermark.main.utils.ReadFile;
import com.watermark.main.utils.WriteFile;
import com.watermark.main.utils.wmoperate.JudgeUtils;
import com.watermark.main.utils.wmoperate.WaterMarking;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/user")
public class UserFuncController {
    final
    DataSourceRepository dataSourceRepository;
    final
    WaterMarkKeyRepository waterMarkKeyRepository;
    final
    WaterMarkKeyService waterMarkKeyService;
    final
    LogInfoService logInfoService;

    public UserFuncController(DataSourceRepository dataSourceRepository, WaterMarkKeyRepository waterMarkKeyRepository, WaterMarkKeyService waterMarkKeyService, LogInfoService logInfoService) {
        this.dataSourceRepository = dataSourceRepository;
        this.waterMarkKeyRepository = waterMarkKeyRepository;
        this.waterMarkKeyService = waterMarkKeyService;
        this.logInfoService = logInfoService;
    }

    /**
     * 水印嵌入.
     * @return /user/embed
     */
    @GetMapping("/toEmbed")
    public String toEmbed() {
        return  "/user/embed";
    }
    @PostMapping("/embed")
    public String embed(@RequestParam("file") MultipartFile file, @RequestParam("markedline") String ml,
                        @RequestParam("key") String K, @RequestParam("M") String m, ModelMap mp) throws Exception {
        //判断文件是否为空
        if(file.isEmpty()){
            mp.addAttribute("result_file", "上传失败，请选择合适的文件上传！");
            logInfoService.save("上传空文件，失败", 0, 3);
            return "/user/embed";
        }
        //转换MutipartFile类型为File类型
        File toFile = ReadFile.multipartFileToFile(file);
        if (ml == null) {
            mp.addAttribute("result_file", "上传失败，请输入属性列号！！");
            logInfoService.save("未输入属性列号", 0, 3);
            return "/user/embed";
        }

        //获取上传者用户名
        UserInfo user = (UserInfo) SecurityUtils.getSubject().getPrincipal();
        String username = user.getUsername();
        mp.addAttribute("username", username);
        //获取时间戳和日期
        long currentTime=new Date().getTime();
        SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        String date = format.format(currentTime);

        try {
            //指定上传的位置为 d:/upload/
            String path = "F:/TMP/";

            //获取上传时的文件名
            String originalFilename = file.getOriginalFilename();
            //设置存到服务器的文件名:hash(原文件名+时间戳)，但这样后缀名没了
            String fileName = originalFilename + currentTime;
            Md5Hash hashName = new Md5Hash(fileName);
            //注意是路径+hashName
            File targetFile = new File(path + hashName.toString());
            //如果之前的 String path = "d:/upload/" 没有在最后加 / ，那就要在 path 后面 + "/"

            //判断文件父目录是否存在
            if(!targetFile.getParentFile().exists()){
                //不存在就创建一个
                targetFile.getParentFile().mkdir();
            }

            //转换MutipartFile类型为File类型
            Dataset dataset;
            //读取并处理上传的文件为dataset类型
            dataset = ReadFile.readTable(toFile);

            //调用嵌入API
            WaterMarking waterMark = new WaterMarking();
            Double M = Double.parseDouble(m);
            Integer markedLine = Integer.parseInt(ml) - 1;

            //判断markedLine是否超过最大列，以及所选列是否是数值型
            JudgeUtils judgeUtils = new JudgeUtils();
            judgeUtils.JudgeMarkedLine(dataset, markedLine);

            Dataset embedDataset = waterMark.Embed(K, M, dataset, markedLine);

            //转换dataset类型为String并写入到服务器指定，路径
            WriteFile.WriteTable(embedDataset, targetFile);

            //将文件信息保存到数据库
            //为了查询下载提供密钥，还需要保存密钥信息到datasource数据库
            DataSource dataSource = this.saveDataSourceFile(hashName.toString(),originalFilename,
                    path + hashName.toString(),date,user,K,M,markedLine+1);

            //保存密钥K,M,markedLine到数据库
            this.saveKeyInfo(K,M,markedLine+1,user,dataSource);

            //告诉页面上传成功了
            mp.addAttribute("result_file", "嵌入成功");
            logInfoService.save("上传文件并嵌入水印", 1, 0);
        } catch (IOException e) {
            e.printStackTrace();
            //出现异常，则告诉页面失败
            mp.addAttribute("result_file", "上传失败");
            logInfoService.save("上传文件失败", 0, 3);
        } catch (Exception e1) {
            e1.printStackTrace();
            mp.addAttribute("result_file", "没有该列或者该列属性类型不是数值型！");
            logInfoService.save("属性列不符合规定", 0, 3);
        } finally {
            // 会在本地产生临时文件，用完后需要删除
            if (toFile.exists()) {
                toFile.delete();
            }
        }

        return  "/user/embed";
    }

    /**
     * 水印密钥管理-列出当前用户的水印密钥.
     * @return /user/keymanager
     */
    @RequestMapping("/keymanager")
    public String keyManager(ModelMap mp, @RequestParam(value = "pageNum", defaultValue = "0") int pageNum, @RequestParam(value = "pageSize", defaultValue = "7") int pageSize) {
        //获取上传者用户名
        UserInfo user = (UserInfo) SecurityUtils.getSubject().getPrincipal();
        Page<WaterMarkKey> waterMarkKeyList = waterMarkKeyService.getUserKeyList(user, pageNum, pageSize);
        mp.addAttribute("waterMarkKeyList", waterMarkKeyList);
        logInfoService.save("浏览密钥管理界面", 1, 1);
        return "/user/keymanager";
    }

    /**
     * 水印密钥管理-由用户删除不想存储的密钥.
     * @return /user/keymanager
     */
    @RequestMapping("/delete")
    public String deleteKey(Long id) {
        //System.out.println(id);
        waterMarkKeyService.delete(id);
        logInfoService.save("删除密钥",1,0);
        return "redirect:/user/keymanager";
    }

    //将文件信息保存到数据库
    private DataSource saveDataSourceFile(String HashName, String OriginFileName,
                                    String Url, String UploadTime, UserInfo User, String K, Double M, Integer L) {
        DataSource dataSource = new DataSource();
        dataSource.setHashName(HashName);
        dataSource.setOriginFileName(OriginFileName);
        dataSource.setUrl(Url);
        dataSource.setUploadTime(UploadTime);
        dataSource.setUser(User);
        dataSource.setK(K);
        dataSource.setM(M);
        dataSource.setL(L);
        dataSourceRepository.save(dataSource);
        return dataSource;
    }

    //将用户使用的密钥K,M,markedLine保存到数据库
    private void saveKeyInfo(String K, Double M, Integer markedLine, UserInfo user, DataSource dataSource) {
        WaterMarkKey waterMarkKey = new WaterMarkKey();
        waterMarkKey.setKey(K);
        waterMarkKey.setM(M);
        waterMarkKey.setMarkedLine(markedLine);
        waterMarkKey.setUser(user);
        waterMarkKey.setFile(dataSource);
        waterMarkKeyRepository.save(waterMarkKey);
    }
}
