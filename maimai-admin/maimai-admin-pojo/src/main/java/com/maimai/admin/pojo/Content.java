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
@Getter
@Setter
@ToString
@Table(name = "tb_content")
public class Content implements Serializable {

	/** 编号 */
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	/** 内容分类ID */
    @Column(name = "category_id")
    private Long categoryId;
    /** 内容标题 */
    private String title;
    /** 子标题 */
    @Column(name = "sub_title")
    private String subTitle;
    /** 标题描述 */
    @Column(name = "title_desc")
    private String titleDesc;
    /** 链接 */
    private String url;
    /** 图片绝对路径 */
    private String pic;
    /** 图片2 */
    private String pic2;
    /** 内容 */
    private String content;
    /** 创建时间 */
    @Column
    private Date created;
    /** 更新时间 */
    @Column
    private Date updated;
}