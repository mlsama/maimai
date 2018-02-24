package com.maimai.item.controller;

import com.maimai.admin.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 描述:商品详情控制类
 *
 * @Author:mlsama 2018/2/7 22:55
 */
@Controller
@Slf4j
public class ItemController {
    /** 注入商品服务 */
    @Autowired
    private ItemService itemService;

    /**
     * 根据商品id查询商品
     * @param id 商品id
     * @param model 模型
     * @return ftl页面.已经配置了基本路径
     */
    @GetMapping("/{id}")
    public String getItem(@PathVariable("id") Long id, Model model){
        /** 根据id查询商品 */
        model.addAttribute("item", itemService.selectByPrimaryKey(id));
        log.info("根据id查询商品结果{}",itemService.selectByPrimaryKey(id));
        /** 根据商品id查询商品描述 */
        model.addAttribute("itemDesc", itemService.selectItemDesc(id));
        log.info("根据id查询商品描述结果{}",itemService.selectItemDesc(id));
        /** 返回视图 */
        return "item";
    }
}
