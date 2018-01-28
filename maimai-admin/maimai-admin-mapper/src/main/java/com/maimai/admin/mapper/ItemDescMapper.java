package com.maimai.admin.mapper;

import com.maimai.admin.pojo.ItemDesc;
import tk.mybatis.mapper.common.Mapper;

/**
 * mlsama
 * 2017/12/10 22:19
 * 描述:商品描述mapper
 */
public interface ItemDescMapper extends Mapper<ItemDesc>{

    public ItemDesc selectItemDesc(Long id);
}
