package com.maimai.admin.service;

import com.maimai.admin.pojo.Content;
import com.maimai.common.vo.DataGridResult;

/**
 * mlsama
 * 2017/12/21 22:47
 * 描述:内容接口
 */
public interface ContentService extends BaseService<Content> {

    DataGridResult selectContentByPage(Long categoryId, Integer page, Integer rows);

    String findBigAdData();
}
