package com.maimai.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * mlsama
 * 2017/12/8 22:14
 * 描述:页面跳转控制类
 */
@Controller
public class PageForwardController {

    /**
     * 通用跳转方法
     * 页面路径:'/page/content-category'
     * content-category:JSP页面名称
     */
    @RequestMapping("/page/{viewName}")
    public String commonPageForward(@PathVariable("viewName") String viewName){
        return viewName;
    }
}
