package com.watermark.main.controller;

import com.watermark.main.testfunction.Add;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserInfoController {
    /**
     * 水印嵌入.
     * @return
     */
    @RequestMapping("/embed")
    public String embed(ModelMap mp) {
        Add a = new Add();
        Integer res = a.add(1,2);
        mp.addAttribute("res", res);
        return  "/user/embed";
    }
}
