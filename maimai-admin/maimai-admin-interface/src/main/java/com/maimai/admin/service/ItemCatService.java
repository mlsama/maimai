package com.maimai.admin.service;

import com.maimai.admin.pojo.ItemCat;

import java.util.List;
import java.util.Map;

/**
 * mlsama
 * 2017/12/7 21:51
 * 描述:接口
 */
public interface ItemCatService extends BaseService<ItemCat> {

    public List<Map<String,Object>> getItemCatByParentId(Long parentId);
}
