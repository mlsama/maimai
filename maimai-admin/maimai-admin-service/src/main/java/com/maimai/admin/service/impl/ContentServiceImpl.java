package com.maimai.admin.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.maimai.admin.mapper.ContentMapper;
import com.maimai.admin.pojo.Content;
import com.maimai.admin.service.ContentService;
import com.maimai.common.constant.Constant;
import com.maimai.common.redis.RedisService;
import com.maimai.common.vo.DataGridResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * mlsama
 * 2017/12/21 23:13
 * 描述:内容接口实现类
 */
@Service
@Transactional
@Slf4j
public class ContentServiceImpl extends BaseServiceImpl<Content>
        implements ContentService{
    @Autowired
    private ContentMapper contentMapper;
    //redis
    @Autowired
    private RedisService redisService;

    /**
     * 查询内容
     * @param categoryId
     * @param page
     * @param rows
     * @return
     */
    @Override
    public DataGridResult selectContentByPage(Long categoryId, Integer page, Integer rows) {
        //添加分页条件
        Example example = new Example(Content.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("categoryId",categoryId);
        //开启分页查询
        PageHelper.startPage(page,rows);
        PageInfo<Content> pageInfo = new PageInfo<>(contentMapper.selectByExample(example));
        DataGridResult dataGridResult = new DataGridResult();
        dataGridResult.setTotal(pageInfo.getTotal());
        dataGridResult.setRows(pageInfo.getList());
        return dataGridResult;
    }

    /**
     * 获取大广告数据
     * @return  JSON格式字符串
     */
    @Override
    public String findBigAdData() {
        try {
            log.info("查询打广告数据开始");
            //先去redis中取
            String data = redisService.get(Constant.BIGAD_DATA);
            if (StringUtils.isNoneBlank(data)){
                return data;
            }
            log.info("从redis中查询大广告数据结果为{}",data);
            List<Content> list = (List<Content>) selectContentByPage(12L, 1, 6).getRows();
            /**
             *  [{
             "alt": "",
             "height": 240,
             "heightB": 240,
             "href": "",
             "src": "",
             "srcB": "",
             "width": 670,
             "widthB": 550
             },{}]
             */
            List<Map<String, Object>> dataLists = new ArrayList<>();
            for (Content content : list){
                Map<String, Object> map = new HashMap<>();
                map.put("alt", content.getTitle());
                map.put("height", 240);
                map.put("heightB", 240);
                map.put("href", content.getUrl());
                map.put("src", content.getPic());
                map.put("srcB", content.getPic2());
                map.put("width", 670);
                map.put("widthB", 550);
                dataLists.add(map);
            }
            String result = new ObjectMapper().writeValueAsString(dataLists);
            //存入redis
            redisService.setex(Constant.BIGAD_DATA,result,60*60*24);
            return result;
        }catch (Exception e){
            throw new RuntimeException("查询大广告数据异常:{}"+e);
        }
    }

}
