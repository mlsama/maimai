package com.maimai.admin.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 商品描述
 */
@Table(name = "tb_item_desc")
@Setter
@Getter
@ToString
public class ItemDesc implements Serializable{
    
	/** 商品ID,对应tb_item中的id */
	@Id @Column(name = "item_id")
    private Long itemId;
	/** 商品描述 */
    @Column(name = "item_desc")
    private String itemDesc;
    /** 创建时间 */
    @Column
    private Date created;
    /** 更新时间 */
    @Column
    private Date updated;
}