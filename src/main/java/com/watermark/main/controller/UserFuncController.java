package com.watermark.main.controller;

import com.watermark.main.DPWA.Dataset;
import com.watermark.main.entity.DataSource;
import com.watermark.main.entity.UserInfo;
import com.watermark.main.repository.DataSourceRepository;
import com.watermark.main.testfunction.Add;
import com.watermark.main.utils.ReadFile;
import com.watermark.main.utils.WriteFile;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
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

    public UserFuncController(DataSourceRepository dataSourceRepository) {
        this.dataSourceRepository = dataSourceRepository;
    }

    /**
     * 水印嵌入.
     * @return
     */
    @GetMapping("/toEmbed")
    public String toEmbed() {
        return  "/user/embed";
    }
    @PostMapping("/embed")
    public String embed(@RequestParam("file") MultipartFile file, @RequestParam("markedline") String text, ModelMap mp) throws Exception {
        System.out.println(text);
        //判断文件是否为空
        if(file.isEmpty()){
            mp.addAttribute("result_file", "上传失败，请选择合适的文件上传！");
            return "/user/embed";
        }
        //转换MutipartFile类型为File类型
        File toFile = ReadFile.multipartFileToFile(file);

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
            //TODO：若有后缀名尽可能保存后缀名
            String fileName = originalFilename + currentTime;
            Md5Hash hashName = new Md5Hash(fileName);
            //注意是路径+hashName
            File targetFile = new File(path + hashName.toString());
            //如果之前的 String path = "d:/upload/" 没有在最后加 / ，那就要在 path 后面 + "/"

            //将文件信息保存到数据库
            DataSource dataSource = new DataSource();
            dataSource.setHashName(hashName.toString());
            dataSource.setOriginFileName(originalFilename);
            dataSource.setUrl(path + hashName.toString());
            dataSource.setUploadTime(date);
            dataSource.setUser(user);
            dataSourceRepository.save(dataSource);

            //判断文件父目录是否存在
            if(!targetFile.getParentFile().exists()){
                //不存在就创建一个
                targetFile.getParentFile().mkdir();
            }

            //TODO:调用嵌入算法处理上传的文件
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//            String str = null;
//            while((str = bufferedReader.readLine()) != null)
//            {
//                System.out.println(str);
//            }
            //转换MutipartFile类型为File类型
            Dataset dataset;
            //读取并处理上传的文件为dataset类型
            dataset = ReadFile.readTable(toFile);
            //转换dataset类型为String并写入到服务器指定，路径
            WriteFile.WriteTable(dataset, targetFile);

            //告诉页面上传成功了
            mp.addAttribute("result_file", "上传成功");
        } catch (IOException e) {
            e.printStackTrace();
            //出现异常，则告诉页面失败
            mp.addAttribute("result_file", "上传失败");
        } finally {
            // 会在本地产生临时文件，用完后需要删除
            if (toFile.exists()) {
                toFile.delete();
            }
        }

        Add a = new Add();
        Integer res = a.add(1,2);
        mp.addAttribute("res", res);
        return  "/user/embed";
    }
}