package com.maimai.search.service;

import com.maimai.search.vo.SolrItem;

import java.util.List;
import java.util.Map;

/**
 * 描述:商品搜索接口
 *
 * @Author:mlsama 2018/2/1 21:38
 */
public interface ItemSearchService {
    void saveOrUpdateItemSolr(List<SolrItem> solrItems);

    Map<String,Object> itemSearch(String keyword, Integer page,Integer rows) throws Exception;

    void delete(String[] idArr) throws Exception;
}
