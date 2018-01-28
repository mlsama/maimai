package com.maimai.admin.controller;

import com.maimai.admin.pojo.Item;
import com.maimai.admin.pojo.ItemDesc;
import com.maimai.admin.service.ItemService;
import com.maimai.common.vo.DataGridResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.util.Arrays;

/**
 * mlsama
 * 2017/12/10 22:11
 * 描述: 商品控制类
 */
@Controller
@ResponseBody
@RequestMapping("/item")
public class ItemController {
    @Autowired
    private ItemService itemService;
    /**
     * 添加商品
     * @param item
     */
    @RequestMapping("/save")
    public void saveItem(Item item,@RequestParam("desc")String desc){
        try {
            itemService.saveItem(item,desc);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 回显商品描述
     * @param id
     * @return
     */
    @RequestMapping("/itemdesc/{id}")
    public ItemDesc getItemDescById(@PathVariable("id") Long id){
        return itemService.selectItemDesc(id);
    }
    /**
     * 修改商品
     * @param item
     */
    @RequestMapping("/update")
    public void updateItem(Item item,@RequestParam("desc")String desc){
        try {
            itemService.updateItem(item,desc);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 查询
     */
    @GetMapping
    public DataGridResult selectItemByPage(Item item,
                                           @RequestParam("page")Integer page,
                                           @RequestParam("rows")Integer rows){
        try{
            //商品title已经URI加密,需要解密
            if (item != null && StringUtils.isNoneBlank(item.getTitle())) {
                item.setTitle(URLDecoder.decode(item.getTitle(),"utf-8"));
            }
            return itemService.selectItemByPage(item,page,rows);

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public void deleteItem(@RequestParam("ids") String ids){
        if (StringUtils.isNoneBlank(ids)){
            String[] strId = ids.split(",");
            itemService.deleteBatch("id",strId);
        }else {
            throw new RuntimeException("ids为空");
        }

    }

    /**
     * 上,下架
     * @param ids
     */
    @RequestMapping("/instockOrReshelf")
    public void instockOrReshelfItem(String ids,String option){
        if (StringUtils.isNoneBlank(ids)) {
            String[] id = ids.split(",");
            itemService.instockOrReshelfItem(Arrays.asList(id),option);
        }else {
            throw new RuntimeException("ids为空");
        }
    }

}
