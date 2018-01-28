package com.maimai.admin.service;

import java.io.Serializable;
import java.util.List;

/**
 * mlsama
 * 2017/12/9 14:28
 * 描述:基础接口
 */
public interface BaseService<T> {

    public void saveSelective(T entity);
    public void deleteByPrimaryKey(Serializable id);
    public void deleteBatch(String idFiled,Serializable[] ids);
    public void updateSelective(T entity);
    public T selectByPrimaryKey(Serializable id);
    public List<T> selectAll();
    public List<T> selectByWhere(T entity);
    public int countByWhere(T entity);
    public List<T> selectByPage(Integer pageNum,Integer pageSize);



}
