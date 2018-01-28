package com.maimai.admin.controller;

import com.maimai.admin.pojo.ContentCategory;
import com.maimai.admin.service.ContentCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * mlsama
 * 2017/12/19 20:20
 * 描述:内容类目控制类
 */
@Controller
@ResponseBody
@RequestMapping("/contentcategory")
@Slf4j
public class ContentCategoryController {
    @Autowired
    private ContentCategoryService contentCategoryService;
    /**
     * 根据父节点ID查找内容分类
     * @param parentId
     * @return
     */
    @GetMapping
    public List<Map<String,Object>> selectContentCategoryByParentid(
            @RequestParam(value="id",defaultValue = "0")Long parentId){
        log.info("传进来的父节点ID为:"+parentId);
        try {
            return contentCategoryService.selectContentCategoryByParentid(parentId);
        }catch (Exception e){
            log.info("父节点ID查找内容类目异常"+e.toString());
        }
        return null;
    }

    /**
     * 添加内容类目.页面会把我们输入的name和选中的parentId传过来
     * @param contentCategory
     * @return
     */
    @PostMapping("/save")
    public Long save(ContentCategory contentCategory){
        log.info("添加内容类目请求参数:"+contentCategory);
        try {
            return contentCategoryService.save(contentCategory);
        }catch (Exception e){
            log.info("添加内容类目异常"+e.toString());
            throw new RuntimeException();
        }
    }
    /**
     * 修改内容类目.直接调用接口方法就行.
     * @param contentCategory
     * @return
     */
    @PostMapping("/update")
    public void update(ContentCategory contentCategory){
        log.info("修改内容类目请求参数:"+contentCategory);
        try {
            contentCategoryService.updateSelective(contentCategory);
        }catch (Exception e){
            log.info("修改内容类目异常"+e.toString());
            throw new RuntimeException();
        }
    }
    /**
     * 删除内容类目.直接调用接口方法就行.
     * @param contentCategory
     * @return
     */
    @PostMapping("/delete")
    public void delete(ContentCategory contentCategory){
        log.info("删除内容类目请求参数:"+contentCategory);
        try {
            contentCategoryService.delete(contentCategory);
        }catch (Exception e){
            log.info("删除内容类目异常"+e.toString());
            throw new RuntimeException();
        }
    }
}
