package com.maimai.admin.controller;

import com.maimai.admin.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * mlsama
 * 2017/12/7 22:09
 * 描述:控制类
 */
//@Controller和@ResponseBody的合体
@RestController
@RequestMapping("/itemcat")
public class ItemCatController {
    @Autowired
    private ItemCatService itemCatService;

    /**
     * 根据父节点获取商品类目
     * @return
     */
    @GetMapping
    public List<Map<String,Object>> getItemCatByParentId(@RequestParam(value="id",
                            defaultValue = "0") Long parentId){
        return itemCatService.getItemCatByParentId(parentId);
    }
}
