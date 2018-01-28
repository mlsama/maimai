package com.maimai.admin.mapper;

import com.maimai.admin.pojo.ItemCat;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 *通用mapper
 */
public interface ItemCatMapper extends Mapper<ItemCat>{

    public List<Map<String,Object>> getItemCatByParentId(Long parentId);
}
