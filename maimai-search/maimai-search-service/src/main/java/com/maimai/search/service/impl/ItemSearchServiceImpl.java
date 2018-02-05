package com.maimai.search.service.impl;

import com.maimai.search.service.ItemSearchService;
import com.maimai.search.vo.SolrItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:商品搜索接口实现类
 *
 * @Author:mlsama 2018/2/1 21:39
 */
@Service
public class ItemSearchServiceImpl implements ItemSearchService {
    /** 注入SolrClient */
    @Autowired
    private SolrClient solrClient;
    @Value("${defaultCollection}")
    private String defaultCollection;
    /**
     * 批量添加或修改索引
     * @param solrItems 集合
     */
    @Override
    public void saveOrUpdateItemSolr(List<SolrItem> solrItems){
        try{
            /** 批量添加，返回修改响应对象 */
            UpdateResponse response = solrClient
                    .addBeans(defaultCollection,solrItems);
            /** 判断状态码: 0 代表成功 */
            if (response.getStatus() == 0){
                /** 提交事务 */
                solrClient.commit(defaultCollection);
            }else{
                /** 回滚事务 */
                solrClient.rollback(defaultCollection);
            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 商品搜索:页面发来查询关键字和当前页数.
     * @param keyword
     * @param page
     * @param rows 每页记录数
     * @return map
     */
    @Override
    public Map<String, Object> itemSearch(String keyword, Integer page,Integer rows) throws Exception {
        //默认搜索全部
        if (StringUtils.isBlank(keyword)){
            keyword = "*";
        }
        /** 创建SolrQuery封装查询参数 */
        SolrQuery solrQuery = new SolrQuery();
        /** 查询字符串 */
        solrQuery.setQuery("title:" + keyword + " AND status:1");
        /** 设置分页开始记录数  limit的第一个号*/
        solrQuery.setStart((page - 1) * rows);
        /** 设置每页显示的记录数 */
        solrQuery.setRows(rows);

        /** 判断是否需要高亮显示 */
        if (!"*".equals(keyword)){
            /** 设置高亮显示 */
            solrQuery.setHighlight(true);
            /** 设置高亮显示字段 */
            solrQuery.addHighlightField("title");
            /** 设置文本截断 */
            solrQuery.setHighlightFragsize(60);
            /** 设置高亮格式器前缀 */
            solrQuery.setHighlightSimplePre("<font color='red'>");
            /** 设置高亮格式器前缀 */
            solrQuery.setHighlightSimplePost("</font>");
        }

        /** 检索，得到查询响应对象 */
        QueryResponse response = solrClient.query(defaultCollection, solrQuery);
        /** 定义Map集合封装数据 */
        Map<String, Object> data = new HashMap<>();
        /** 判断响应状态码 */
        if (response.getStatus() == 0){
            /** 获取总命中的记录数 */
            long count = response.getResults().getNumFound();
            // ${totalPage}: 总页数
            // count % rows == 0 ? count / rows : (count / rows) + 1
            // ((count - 1) / rows) + 1
            data.put("totalPage", ((count - 1) / rows) + 1);

            // ${itemList}: 商品集合List<SolrItem>
            List<SolrItem> itemList = response.getBeans(SolrItem.class);
            /** 判断是否需要高亮: title要高亮 */
            if (solrQuery.getHighlight()){
                /** 获取高亮内容 */
                Map<String, Map<String, List<String>>> hMaps = response.getHighlighting();
                /** 迭代itemList集合 */
                for (SolrItem solrItem : itemList){
                    /** 获取标题高亮内容 */
                    String title = hMaps.get(String.valueOf(solrItem.getId())).get("title").get(0);
                    /** 设置标题内容为高亮后的内容 */
                    solrItem.setTitle(title);
                }
            }
            data.put("itemList", itemList);
        }
        return data;
    }

    /**
     * 批量删除索引
     * @param idArr id数字组
     */
    @Override
    public void delete(String[] idArr) throws Exception{
        if (idArr != null && idArr.length > 0){
            solrClient.deleteById(Arrays.asList(idArr));
        }
    }
}
