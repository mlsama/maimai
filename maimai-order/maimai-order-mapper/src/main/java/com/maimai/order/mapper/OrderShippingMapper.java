package com.maimai.order.mapper;

import com.maimai.order.pojo.OrderShipping;

/**
 * 描述:订单物流数据库访问接口
 *
 * @Author:mlsama 2018/2/20 12:50
 */
public interface OrderShippingMapper {

    OrderShipping findOrderShippingByOrderId(String orderId);
}
