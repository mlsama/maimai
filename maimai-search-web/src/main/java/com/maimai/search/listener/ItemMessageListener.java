package com.maimai.search.listener;

import java.util.ArrayList;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.listener.SessionAwareMessageListener;

import com.maimai.admin.pojo.Item;
import com.maimai.admin.service.ItemService;
import com.maimai.search.service.ItemSearchService;
import com.maimai.search.vo.SolrItem;

/**
 * 商品消息监听器
 * SessionAwareMessageListener接口是有事务的,并且可以指定消息类型
 */
@Slf4j
public class ItemMessageListener implements SessionAwareMessageListener<MapMessage> {

	@Autowired
	private ItemService itemService;
	@Autowired
	private ItemSearchService itemSearchService;
	
	@Override
	public void onMessage(MapMessage message, Session session) throws JMSException {
		try{
			/** 获取消息体中的内容 */
			String target = message.getString("target");
			/** 商品id */
			Object id = message.getObject("id");
			log.info("接收mq信息结果为target:{},id:{}",target,id);
			if ("delete".equals(target)){ // 批量删除
				/** 删除索引库中的索引  id: 1,2,3*/
				String ids = (String)id;
				/** 分隔成多个id */
				String[] idArr = ids.split(",");
				/** 批量删除Solr索引库数据 */
				itemSearchService.delete(idArr);
			}else{ // 添加或修改
				Long itemId = (Long)id;
				/** 调用商品的服务层获取商品 */
				Item item = itemService.selectByPrimaryKey(itemId);
				SolrItem solrItem = new SolrItem();
				solrItem.setId(item.getId());
				solrItem.setImage(item.getImage());
				solrItem.setPrice(item.getPrice());
				solrItem.setSellPoint(item.getSellPoint());
				solrItem.setStatus(item.getStatus());
				solrItem.setTitle(item.getTitle());
				
				List<SolrItem> solrItems = new ArrayList<>();
				solrItems.add(solrItem);
				/** 调用商品搜索服务添加或修改索引 */
				itemSearchService.saveOrUpdateItemSolr(solrItems);
			}
			/** 提交消息事务 */
			session.commit();
		}catch(Exception ex){
			/** 回滚消息事务 */
			session.rollback();
			ex.printStackTrace();
		}
	}
}