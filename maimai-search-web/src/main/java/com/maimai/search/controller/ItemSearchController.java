package com.maimai.search.controller;

import com.maimai.search.service.ItemSearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 描述:商品搜索控制类
 *
 * @Author:mlsama 2018/2/3 15:32
 */
@Controller
public class ItemSearchController {

    @Autowired
    private ItemSearchService itemSearchService;
    /**
     * 商品搜索:页面发来查询关键字和当前页数.
     * 设定每页显示20条记录
     * @param keyword 搜索关键字
     * @param page 当前页
     * @param model
     * @return 页面
     */
    @GetMapping("/search")
    public String itemSearch(@RequestParam(value = "q",required = false) String keyword,
                             @RequestParam(value = "page",defaultValue = "1") Integer page,
                             Model model){
        try {
            if (StringUtils.isNoneBlank(keyword)){
                //用ISO-8859-1解码，再用UTF-8编码
                keyword = new String(keyword.getBytes("ISO-8859-1"),"UTF-8");
            }
            //调用搜索方法
            Map<String,Object> map = itemSearchService.itemSearch(keyword,page,20);
            //返回数据给页面
            model.addAttribute("query",keyword);
            model.addAttribute("page",page);
            if (map != null && map.size() > 0){
                /**
                 *   addAllAttributes:把map中的键值对返回给页面,类似于:
                 *   model.addAttribute("totalPage",totalPage);
                 *   model.addAttribute("itemList",itemList);
                 */
                model.addAllAttributes(map);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "search";
    }
}
