package com.maimai.admin.service.impl;

import com.maimai.admin.mapper.ItemCatMapper;
import com.maimai.admin.service.ItemCatService;
import com.maimai.admin.pojo.ItemCat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


/**
 * mlsama
 * 2017/12/7 21:57
 * 描述:接口实现类
 */
@Service
//事务
@Transactional
public class ItemCatServiceImpl extends BaseServiceImpl<ItemCat>
        implements ItemCatService {
    @Autowired
    private ItemCatMapper itemCatMapper;

    @Override
    public List<Map<String, Object>> getItemCatByParentId(Long parentId) {
        List<Map<String, Object>> itemCats = itemCatMapper.getItemCatByParentId(parentId);
        for (Map<String,Object> map : itemCats) {
            boolean state = (boolean)map.get("state");
            map.put("state", state ? "closed" : "open");
        }
        return itemCats;
    }
}
