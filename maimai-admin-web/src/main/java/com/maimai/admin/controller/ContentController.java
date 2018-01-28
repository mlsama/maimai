package com.maimai.admin.controller;

import com.maimai.admin.pojo.Content;
import com.maimai.admin.service.ContentService;
import com.maimai.common.vo.DataGridResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * mlsama
 * 2017/12/21 22:45
 * 描述:内容控制类
 */
@Controller
@ResponseBody
@RequestMapping("/content")
@Slf4j
public class ContentController {
    @Autowired
    private ContentService contentService;

    /**
     * 查询
     * @param categoryId
     * @param page
     * @param rows
     * @return
     */
    @GetMapping
    public DataGridResult selectContentByPage(
            @RequestParam(value="categoryId", defaultValue="0")Long categoryId,
            @RequestParam(value="page", defaultValue="1")Integer page,
            @RequestParam(value="rows", defaultValue="15")Integer rows){
        log.info("分页查询内容参数:"+categoryId+","+page+","+rows);
        try {
            return contentService.selectContentByPage(categoryId,page,rows);
        }catch (Exception e){
            log.info("分页查询内容异常"+e);
        }
        return null;
    }

    /**
     * 添加
     * @param content
     */
    @PostMapping("/save")
    public void save(Content content){
        log.info("添加内容参数为:"+content);
        try {
            content.setCreated(new Date());
            content.setUpdated(content.getCreated());
            log.info("添加的内容对象为:"+content);
            contentService.saveSelective(content);
        }catch (Exception e){
            log.info("添加内容异常"+e);
        }
    }

    /**
     * 修改内容
     * @param content
     */
    @PostMapping("/update")
    public void update(Content content){
        log.info("修改内容参数为:"+content);
        try {
            content.setUpdated(new Date());
            log.info("修改后的内容为:"+content);
            contentService.updateSelective(content);
        }catch (Exception e){
            log.info("修改内容异常"+e);
        }
    }
    @PostMapping("/delete")
    public void delete(String ids){
        log.info("删除内容参数为:"+ids);
        try {
            if (StringUtils.isNoneBlank(ids)){
                contentService.deleteBatch("id",ids.split(","));
            }else {
                throw new RuntimeException("传入的参数为空.");
            }
        }catch (Exception e){
            log.info("删除内容异常"+e);
        }
    }
}
