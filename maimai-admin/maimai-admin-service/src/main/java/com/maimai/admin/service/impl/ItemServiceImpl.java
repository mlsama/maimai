package com.maimai.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.maimai.admin.mapper.ItemDescMapper;
import com.maimai.admin.mapper.ItemMapper;
import com.maimai.admin.pojo.Item;
import com.maimai.admin.pojo.ItemDesc;
import com.maimai.admin.service.ItemService;
import com.maimai.common.vo.DataGridResult;
import jdk.nashorn.internal.runtime.regexp.joni.constants.OPCode;
import net.sf.jsqlparser.expression.LongValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * mlsama
 * 2017/12/10 22:24
 * 描述:商品实现类
 */
@Service
@Transactional
public class ItemServiceImpl extends BaseServiceImpl<Item>
        implements ItemService {
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ItemDescMapper itemDescMapper;

    /**
     * 添加商品
     * @param item
     * @param desc
     */
    @Override
    public Long saveItem(Item item, String desc) {
        //保存商品
        item.setCreated(new Date());
        item.setUpdated(new Date());
        itemMapper.insertSelective(item);
        //保持商品描述
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(new Date());
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        itemDescMapper.insertSelective(itemDesc);
        return item.getId();
    }

    @Override
    public ItemDesc selectItemDesc(Long id) {
        return itemDescMapper.selectItemDesc(id);
    }

    /**
     * 修改商品
     * @param item
     * @param desc
     */
    @Override
    public void updateItem(Item item, String desc) {
        //修改商品
        item.setUpdated(new Date());
        itemMapper.updateByPrimaryKeySelective(item);
        //修改商品描述
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setUpdated(new Date());
        itemDesc.setItemDesc(desc);
        itemDescMapper.updateByPrimaryKeySelective(itemDesc);
    }

    /**
     * 查询商品:全表或条件
     * @param item
     * @param page  当前页
     * @param rows  每页大小
     * @return
     */
    @Override
    public DataGridResult selectItemByPage(Item item, Integer page, Integer rows) {
        PageHelper.startPage(page,rows);
        PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(itemMapper.selectItem(item));
        DataGridResult result = new DataGridResult();
        result.setRows(pageInfo.getList());
        result.setTotal(pageInfo.getTotal());
        return result;

    }

    /**
     * 上,下架
     * @param strings
     */
    @Override
    public void instockOrReshelfItem(List<String> strings,String option) {
        ArrayList<Long> list = new ArrayList<>();
        for (String string: strings) {
            list.add(Long.parseLong(string));
        }
        try {
            itemMapper.updateItemStatus(list,option);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}
