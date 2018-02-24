package com.maimai.order.mapper;

import com.maimai.order.pojo.OrderItem;

/**
 * 描述:订单商品数据库访问接口
 *
 * @Author:mlsama 2018/2/20 12:48
 */
public interface OrderItemMapper {

    OrderItem findOrderItemByOrderId(String orderId);

}
