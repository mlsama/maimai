package com.maimai.admin.mapper;

import com.maimai.admin.pojo.ContentCategory;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * mlsama
 * 2017/12/19 20:36
 * 描述:内容类目mapper
 */
public interface ContentCategoryMapper extends Mapper<ContentCategory>{

    public List<Map<String,Object>> selectContentCategoryByParentid(Long parentId);
}
