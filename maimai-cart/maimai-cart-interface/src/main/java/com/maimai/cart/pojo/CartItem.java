package com.maimai.cart.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 购物车商品数据传输类
 *
 * @Author:mlsama 2018/2/12 16:50
 */
@Setter
@Getter
@ToString
public class CartItem implements Serializable{

	/** 用户编号 */
	private Long userId;
	/** 商品编号 */
	private Long itemId;
	/** 商品标题 */
	private String itemTitle;
	/** 商品图片 */
	private String itemImage;
	/** 商品价格 */
	private Long itemPrice;
	/** 购买数量 */
	private Integer num;
	/** 创建日期 */
	private Date created;
	/** 修改日期 */
	private Date updated;
}