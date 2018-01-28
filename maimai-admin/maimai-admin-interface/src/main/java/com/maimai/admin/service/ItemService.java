package com.maimai.admin.service;

import com.maimai.admin.pojo.Item;
import com.maimai.admin.pojo.ItemDesc;
import com.maimai.common.vo.DataGridResult;

import java.util.List;
import java.util.Map;

/**
 * mlsama
 * 2017/12/10 22:22
 * 描述:
 */
public interface ItemService extends BaseService<Item>{

    public Long saveItem(Item item, String desc);

    public ItemDesc selectItemDesc(Long id);

    public void updateItem(Item item, String desc);

    public DataGridResult selectItemByPage(Item item, Integer page, Integer rows);

    public void instockOrReshelfItem(List<String> strings,String option);
}
