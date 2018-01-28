package com.maimai.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.maimai.admin.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;

/**
 * mlsama
 * 2017/12/9 23:44
 * 描述:
 */
public class BaseServiceImpl<T> implements BaseService<T> {
    //注入通用mapper
    @Autowired
    private Mapper<T> mapper;
    //定义当前实体类
    private Class<T> entityClass;
    //通过构造方法获取当前实体类
    public BaseServiceImpl(){
        //获取T的类型
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        //获取实际的参数数组,取第一个
        entityClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }

    /**
     * 选择性添加
     * @param entity
     */
    @Override
    public void saveSelective(T entity) {
        mapper.insertSelective(entity);
    }

    /**
     * 根据主键删除
     * @param id
     */
    @Override
    public void deleteByPrimaryKey(Serializable id) {
        mapper.deleteByPrimaryKey(id);
    }

    /**
     * 批量删除:使用where id in(...)实现批量删除
     * @param idFiled
     * @param ids
     */
    @Override
    public void deleteBatch(String idFiled, Serializable[] ids) {
        //创建示范对象
        Example example = new Example(entityClass);
        //创建条件对象
       Criteria criteria = example.createCriteria();
       //添加条件:参数一:属性  参数二:属性的集合
        criteria.andIn(idFiled, Arrays.asList(ids));
        //条件删除
        mapper.deleteByExample(example);
    }

    /**
     * 选择性修改:entity有id则是修改,否则是新增
     * @param entity
     */
    @Override
    public void updateSelective(T entity) {
        mapper.updateByPrimaryKeySelective(entity);
    }

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    @Override
    public T selectByPrimaryKey(Serializable id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 查询所有
     * @return
     */
    @Override
    public List<T> selectAll() {
        return mapper.selectAll();
    }

    /**
     * 条件查询
     * @param entity
     * @return
     */
    @Override
    public List<T> selectByWhere(T entity) {
        return mapper.select(entity);
    }

    /**
     * 条件聚合查询
     * @param entity
     * @return
     */
    @Override
    public int countByWhere(T entity) {
        return mapper.selectCount(entity);
    }

    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public List<T> selectByPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return mapper.selectAll();
    }
}
