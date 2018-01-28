package com.maimai.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 描述:跳转控制器
 *
 * @Author:mlsama 2018/1/25 19:24
 */
@Controller
public class ForwordController {

    @GetMapping("/register")
    public String toRegister(){
        return "register";
    }
    @GetMapping("/login")
    public String toLogin(@RequestParam(value = "redirectURL",required = false)
                           String redirectURL, Model model){
        model.addAttribute("redirectURL",redirectURL);
        return "login";
    }
}
