package com.maimai.admin.mapper;

import com.maimai.admin.pojo.Item;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * mlsama
 * 2017/12/10 22:19
 * 描述:商品mapper
 */
public interface ItemMapper extends Mapper<Item>{
    //查询商品
    public List<Map<String,Object>> selectItem(@Param("item") Item item);
    //上,下架
    public void updateItemStatus(@Param("list") ArrayList<Long> list,
                                 @Param("option") String option);
}
