package com.maimai.admin.controller;

import com.maimai.admin.pojo.Item;
import com.maimai.admin.pojo.ItemDesc;
import com.maimai.admin.service.ItemService;
import com.maimai.common.vo.DataGridResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import java.net.URLDecoder;
import java.util.Arrays;

/**
 * mlsama
 * 2017/12/10 22:11
 * 描述: 商品控制类.添加,修改,删除时发送mq消息同步solr库
 */
@Controller
@ResponseBody
@RequestMapping("/item")
@Slf4j
public class ItemController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * 发送消息方法
     * @param target 执行的操作(save、update、delete)
     * @param id 商品的id (Long，批量删除时是String)
     */
    private void sendItemTopicMessage(final String target,final Object id){
        jmsTemplate.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = new ActiveMQMapMessage();
                mapMessage.setString("target", target);
                mapMessage.setObject("id", id);
                return mapMessage;
            }
        });
    }

    /**
     * 添加商品:服务层发来的Item是没有id的.所以方法才返回id.
     * @param item
     */
    @RequestMapping("/save")
    public void saveItem(Item item,@RequestParam("desc")String desc){
        try {
            Long id = itemService.saveItem(item,desc);
            //发送消息同步索引库
            sendItemTopicMessage("save",id);
            log.info("发送消息target:{},id:{}","save",id);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 回显商品描述
     * @param id
     * @return
     */
    @RequestMapping("/itemdesc/{id}")
    public ItemDesc getItemDescById(@PathVariable("id") Long id){
        return itemService.selectItemDesc(id);
    }
    /**
     * 修改商品
     * @param item
     */
    @RequestMapping("/update")
    public void updateItem(Item item,@RequestParam("desc")String desc){
        try {
            itemService.updateItem(item,desc);
            //发送消息同步索引库
            sendItemTopicMessage("update",item.getId());
            log.info("发送消息target:{},id:{}","update",item.getId());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 查询
     */
    @GetMapping
    public DataGridResult selectItemByPage(Item item,
                                           @RequestParam("page")Integer page,
                                           @RequestParam("rows")Integer rows){
        try{
            //商品title已经URI加密,需要解密
            if (item != null && StringUtils.isNoneBlank(item.getTitle())) {
                item.setTitle(URLDecoder.decode(item.getTitle(),"utf-8"));
            }
            return itemService.selectItemByPage(item,page,rows);

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public void deleteItem(@RequestParam("ids") String ids){
        if (StringUtils.isNoneBlank(ids)){
            String[] strId = ids.split(",");
            itemService.deleteBatch("id",strId);
            //发送消息同步索引库
            sendItemTopicMessage("delete",ids);
            log.info("发送消息target:{},ids:{}","delete",ids);
        }else {
            throw new RuntimeException("ids为空");
        }

    }

    /**
     * 上,下架
     * @param ids
     */
    @RequestMapping("/instockOrReshelf")
    public void instockOrReshelfItem(String ids,String option){
        if (StringUtils.isNoneBlank(ids)) {
            String[] id = ids.split(",");
            itemService.instockOrReshelfItem(Arrays.asList(id),option);
        }else {
            throw new RuntimeException("ids为空");
        }
    }

}
