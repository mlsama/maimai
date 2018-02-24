package com.maimai.order.service.impl;

import com.maimai.order.mapper.OrderMapper;
import com.maimai.order.pojo.Order;
import com.maimai.order.service.OrderService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 描述:订单接口实现类
 *
 * @Author:mlsama 2018/2/20 13:26
 */
@Service
@Transactional(readOnly = false)
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    /**
     * 保存订单
     * @param order
     * @return  订单号
     */
    @Override
    public String saveOrder(Order order) {
        //生成订单号
        String orderNum = order.getUserId().toString() + System.currentTimeMillis();
        order.setOrderId(orderNum);
        order.setCreateTime(new Date());
        order.setUpdateTime(order.getCreateTime());
        order.setStatus(1);
        orderMapper.saveOrder(order);
        return orderNum;

    }

    /**
     * 获取订单
     * @param orderId
     * @return
     */
    @Override
    public Order getOrderByOrderId(String orderId) {
        return orderMapper.getOrderByOrderId(orderId);
    }

    /**
     * 关闭1小时前的订单
     */
    @Override
    public void autoCloseOrder() {
        //1个小时前
        Date date = DateTime.now().minusHours(1).toDate();
        orderMapper.closeOrder(date);
    }
}
