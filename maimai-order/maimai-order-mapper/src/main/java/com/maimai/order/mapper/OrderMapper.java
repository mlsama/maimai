package com.maimai.order.mapper;

import com.maimai.order.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * 描述:订单数据库访问接口
 *
 * @Author:mlsama 2018/2/20 12:46
 */
public interface OrderMapper {

    void saveOrder(Order order);

    Order getOrderByOrderId(@Param("orderId") String orderId);

    void closeOrder(Date date);
}
