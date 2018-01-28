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
 * 商品类
 */
@Table(name = "tb_item")
@Setter
@Getter
@ToString
public class Item implements Serializable {
	
    /** 商品id，同时也是商品编号 */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /** 商品标题 */
    @Column
    private String title;
    /** 商品卖点 */
    @Column(name = "sell_point")
    private String sellPoint;
    /** 商品价格，单位为：分 */
    @Column
    private Long price;
    /** 库存数量 */
    @Column
    private Integer num;
    /** 商品条形码 */
    @Column
    private String barcode;
    /** 商品图片 */
    @Column
    private String image;
    /** 所属类目，叶子类目 */
    @Column
    private Long cid;
    /** 商品状态，1-正常，2-下架，3-删除 */
    @Column
    private Integer status;
    /** 创建时间 */
    @Column
    private Date created;
    /** 更新时间 */
    @Column
    private Date updated;
}
