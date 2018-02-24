package com.maimai.item.listener;

import com.maimai.admin.pojo.Item;
import com.maimai.admin.pojo.ItemDesc;
import com.maimai.admin.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述:Springboot整合ActiveMQ,提供监听类监听后台消息.
 *      生成.html文件
 * @Author:mlsama 2018/2/9 22:19
 */
@Component
@Slf4j
public class ItemMessageListener{

    /** 注入FreeMarkerConfigurer */
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    /** 注入服务接口 */
    @Autowired
    private ItemService itemSerivce;
    @Value("${itemHtmlFileDir}")
    private String itemHtmlFileDir;

    /** 处理消息的方法 */
    @JmsListener(destination="item-topic")
    public void readMessage(Map<String, Object> message){

        log.info("item-web: ItemMessageListener消息监听器.....");

        try{
            /** 获取消息中的内容 */
            String target = message.get("target").toString();
            Object id = message.get("id");

            /** 判断是什么样的操作 */
            if ("delete".equals(target)){
                /** 批量删除商品 */
                String ids = (String)id;
                /** 分隔成多个id */
                String[] idArr = ids.split(",");
                /** 迭代文件id，删除静态的html文件 */
                for (String itemId : idArr){
                    File file = new File(itemHtmlFileDir + File.separator + itemId + ".html");
                    if (file.exists() && file.isFile()){
                        file.delete();
                    }
                }
            }else{
                /**
                 * 添加 : 生成静态的html页面
                 * 修改商品 : 生成静态的html页面
                 */
                Configuration configuration = freeMarkerConfigurer.getConfiguration();
                /** 获取指定的模版文件对应的模版对象 */
                Template template = configuration.getTemplate("item.ftl");
                /** 定义Map集合封装数据 */
                Map<String, Object> dataModel = new HashMap<>();

                /** 获取商品id */
                Long itemId = (Long)id;
                /** 根据商品id查询商品 */
                Item item = itemSerivce.selectByPrimaryKey(itemId);
                /** 根据商品id查询商品描述 */
                ItemDesc itemDesc = itemSerivce.selectItemDesc(itemId);

                /** 准备响应数据 */
                dataModel.put("item", item);
                dataModel.put("itemDesc", itemDesc);

                /** 填充模版，生成文件 */
                template.process(dataModel, new FileWriter(
                        new File(itemHtmlFileDir + File.separator + itemId + ".html")));
            }
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
