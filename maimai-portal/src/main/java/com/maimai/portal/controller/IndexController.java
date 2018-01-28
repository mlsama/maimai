package com.maimai.portal.controller;

import com.maimai.admin.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
/**
 * mlsama
 * 2017/12/18 23:50
 * 描述:门户跳转,加载页面的大广告
 */
@Controller
public class IndexController {
    @Autowired
    private ContentService contentService;
    @GetMapping("/index")
    public String toIndex(Model model){
        String bigAdData = contentService.findBigAdData();
        model.addAttribute("bigAdData",bigAdData);
        return "index";
    }

}
