package com.maimai.admin.service;

import com.maimai.admin.pojo.ContentCategory;

import java.util.List;
import java.util.Map;

/**
 * mlsama
 * 2017/12/19 20:31
 * 描述:内容类目接口
 */
public interface ContentCategoryService extends BaseService<ContentCategory>{

    public List<Map<String,Object>> selectContentCategoryByParentid(Long parentId);
    public Long save(ContentCategory contentCategory);

     public void delete(ContentCategory contentCategory);
}
