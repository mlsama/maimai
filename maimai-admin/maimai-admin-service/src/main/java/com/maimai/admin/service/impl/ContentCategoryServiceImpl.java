package com.maimai.admin.service.impl;

import com.maimai.admin.mapper.ContentCategoryMapper;
import com.maimai.admin.pojo.ContentCategory;
import com.maimai.admin.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * mlsama
 * 2017/12/19 20:34
 * 描述:内容分类实现类
 */
@Service
@Transactional
public class ContentCategoryServiceImpl extends BaseServiceImpl<ContentCategory>
        implements ContentCategoryService {
    @Autowired
    private ContentCategoryMapper contentCategoryMapper;
    /**
     * 根据父节点ID查找内容分类
     * @param parentId
     * @return
     */
    @Override
    public List<Map<String, Object>> selectContentCategoryByParentid(Long parentId) {
        List<Map<String, Object>> mapList = contentCategoryMapper.selectContentCategoryByParentid(parentId);
        for (Map<String,Object> map : mapList) {
            boolean state = (boolean) map.get("state");
            //0代表父节点,父节点打开
            map.put("state",state ? "closed" : "open");
        }
        return mapList;
    }

    /**
     * 添加内容类目
     * @param contentCategory
     * @return
     */
    @Override
    public Long save(ContentCategory contentCategory) {
        //添加新类目
        contentCategory.setIsParent(false);
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(new Date());
        contentCategory.setSortOrder(1);
        contentCategory.setStatus(1);
        contentCategoryMapper.insertSelective(contentCategory);
        //更新父类目
        ContentCategory parentCon = new ContentCategory();
        parentCon.setId(contentCategory.getParentId());
        parentCon.setUpdated(new Date());
        parentCon.setIsParent(true);
        contentCategoryMapper.updateByPrimaryKeySelective(parentCon);
        return contentCategory.getId();
    }

    /**
     * 删除本身及其子节点:要先删子节点,所以需要递归删除.最后查询该节点的父节点是否还有子节点
     * 如果没有,设置该节点不是父节点
     * @param contentCategory
     */
    @Override
    public void delete(ContentCategory contentCategory) {
        //定义集合封装需要删除的树节点的id
        List<Long> ids = new ArrayList<>();
        //添加自己的id
        ids.add(contentCategory.getId());
        //递归查询需要删除的子节点,并添加到集合中
        findLeafNode(ids, contentCategory.getId());
        // 删除所有的树节点
        this.deleteBatch("id", ids.toArray(new Long[ids.size()]));

        //查询它的父节点对应的所有子节点
        ContentCategory cc = new ContentCategory();
        cc.setParentId(contentCategory.getParentId());
        //统计查询所有子节点数量
        int count = this.countByWhere(cc);
        if (count == 0){
            //创建父节点/
            ContentCategory parent = new ContentCategory();
            parent.setId(contentCategory.getParentId());
            parent.setIsParent(false);
            parent.setUpdated(new Date());
            //修改父节点
            updateSelective(parent);
        }
    }
    /**
     *递归查询添加子节点的id
     * */
    private void findLeafNode(List<Long> ids, Long id) {
        //创建内容分类对象
        ContentCategory contentCategory = new ContentCategory();
        //添加查询条件
        contentCategory.setParentId(id);
        //根据父节点id查询所有的子节点
        List<ContentCategory> lists = this.selectByWhere(contentCategory);
        if (lists != null && lists.size() > 0){
            //迭代所有的子节点
            for (ContentCategory cc : lists) {
                // 添加需要删除的id
                ids.add(cc.getId());
                //递归查询子节点的所有子节点/
                findLeafNode(ids, cc.getId());
            }
        }
    }
}
