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
@Setter
@Getter
@ToString
@Table(name = "tb_content_category")
public class ContentCategory implements Serializable {

	/** 分类ID */
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	/** 父类目ID=0时，代表的是一级的类目 */
    @Column(name = "parent_id")
    private Long parentId;
    /** 分类名称 */
    private String name;
    /** 状态。可选值:1(正常),2(删除) */
    private Integer status;
    /** 排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数 */
    @Column(name = "sort_order")
    private Integer sortOrder;
    /** 该类目是否为父类目，1为true，0为false */
    @Column(name = "is_parent")
    private Boolean isParent;
    /** 创建时间 */
    @Column
    private Date created;
    /** 更新时间 */
    @Column
    private Date updated;
}