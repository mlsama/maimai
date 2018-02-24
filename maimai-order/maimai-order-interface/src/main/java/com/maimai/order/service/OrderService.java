package com.maimai.order.service;

import com.maimai.order.pojo.Order;

/**
 * 描述:订单接口
 *
 * @Author:mlsama 2018/2/20 13:20
 */
public interface OrderService {

    String saveOrder(Order order);

    Order getOrderByOrderId(String orderId);

    void autoCloseOrder();
}
