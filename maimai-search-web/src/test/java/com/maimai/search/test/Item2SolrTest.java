package com.maimai.search.test;

import com.maimai.admin.pojo.Item;
import com.maimai.admin.service.ItemService;
import com.maimai.search.service.ItemSearchService;
import com.maimai.search.vo.SolrItem;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:把数据库中商品的数据保存到索引库
 *
 * @Author:mlsama 2018/2/1 23:34
 */
public class Item2SolrTest {

    /** 定义ItemSearchService */
    private ItemSearchService itemSearchService;
    private ItemService itemService;
    /** 获取Spring容器 */
    private ApplicationContext ac;

    /** 在测试方法之前 */
    @Before
    public void before() throws Exception {
        /** 获取Spring容器 */
        this.ac = new ClassPathXmlApplicationContext("classpath:maimai-search-web-servlet.xml");
        /** 获取ItemSearchService服务 */
        this.itemSearchService = ac.getBean(ItemSearchService.class);
        /** 获取ItemService服务 */
        this.itemService = ac.getBean(ItemService.class);
    }
    /** 测试查询数据库中的数据全部创建索引 */
    @Test
    public void index() {
        /** 定义当前页码 */
        int page = 1;
        /** 定义每页显示的数据 */
        int rows = 500;
        /** 循环读取数据库中的数据，写入数据库 */
        do {
            System.out.println("开始写第【" + page + "】页的索引");
            /** 查询数据 */
            List<Item> items = itemService.selectByPage(page, rows);
            /** 判断集合 */
            if (items != null && items.size() > 0){
                /** 把List<Item>转化成List<SolrItem> */
                List<SolrItem> solrItems = new ArrayList<SolrItem>();
                /** 迭代items */
                for (Item item : items){
                    SolrItem solrItem = new SolrItem();
                    solrItem.setId(item.getId());
                    solrItem.setImage(item.getImage());
                    solrItem.setPrice(item.getPrice());
                    solrItem.setSellPoint(item.getSellPoint());
                    solrItem.setStatus(item.getStatus());
                    solrItem.setTitle(item.getTitle());
                    solrItems.add(solrItem);
                }
                /** 再把List<SolrItem>添加到索引库 */
                itemSearchService.saveOrUpdateItemSolr(solrItems);
                System.out.println("结束写第【" + page + "】页的索引");
                System.out.println("=========华丽分隔线==========");
                page++;
                rows = items.size();
            }else{
                rows = 0;
            }
        } while (rows == 500);
    }
}
