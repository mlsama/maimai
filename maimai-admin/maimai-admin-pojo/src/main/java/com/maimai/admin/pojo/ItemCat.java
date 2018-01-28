package com.maimai.admin.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * molong
 */
@Table(name = "tb_item_cat")
@Setter
@Getter
@ToString
public class ItemCat implements Serializable {

	/** 类目编号 */
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	/** 类目父级编号 */
    @Column(name = "parent_id")
    private Long parentId;
    /** 类目名称 */
    private String name;
    /** 类目状态 */
    private Integer status;
    /** 类目排序 */
    @Column(name = "sort_order")
    private Integer sortOrder;
    /** 是否为父级 */
    @Column(name = "is_parent")
    private Boolean isParent;
    /** 创建日期 */
    private Date created;
    /** 修改日期 */
    private Date updated;
}