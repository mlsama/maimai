package com.maimai.portal.controller;

import com.maimai.cart.pojo.CartItem;
import com.maimai.cart.service.CartService;
import com.maimai.order.pojo.Order;
import com.maimai.order.service.OrderService;
import com.maimai.sso.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:订单控制类
 *
 * @Author:mlsama 2018/2/19 12:28
 */
@Slf4j
@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;
    /**
     * 跳转到结算页面
     *      需要获取登陆用户购物车的数据
     * @param request
     * @param model
     * @return
     */
    @GetMapping("/balance")
    public String forward2Balance(HttpServletRequest request, Model model){
        System.out.println("============跳转到结算页面开始======================");
        //获取拦截器传来的用户对象
        User user = (User)request.getAttribute("user");
        if (user != null){
            //根据用户id获取用户的购物车数据
            List<CartItem> cartItems = cartService.findCartBuUserId(user.getId());
            model.addAttribute("carts",cartItems);
        }else {
            throw new RuntimeException("拦截器传过来的用户对象为空");
        }
        return "order-cart";
    }

    /** 提交订单 */
    @PostMapping("/submit")
    @ResponseBody
    public Map<String,Object> submitOrder(Order order, HttpServletRequest request){
        /** 创建Map集合封装响应数据 */
        Map<String,Object> map = new HashMap<>();
        map.put("status", 500);
        try{
            /** 获取登录用户 */
            User user = (User)request.getAttribute("user");
            /** 设置购买者信息 */
            order.setUserId(user.getId());
            order.setBuyerNick(user.getUsername());

            /** 保存订单信息 */
            String orderId = orderService.saveOrder(order);

            if (StringUtils.isNotBlank(orderId)){
                map.put("orderId", orderId);
                map.put("status", 200);
                /** 清空当前用户的购物车 (应该是删除订单中对应的商品)*/
                cartService.clearCart(user.getId());
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return map;
    }

    /**
     * 跳转到订单成功页面
     * @param orderId 订单id
     * @param model
     * @return  成功页面
     */
    @RequestMapping("/success")
    public String orderSubmitSuccess(@RequestParam("id")String orderId,Model model){
        Order order = orderService.getOrderByOrderId(orderId);
        model.addAttribute("order",order);
        //送达时间延后2天:使用时间操作框架
        model.addAttribute("date",DateTime.now().plusDays(2).toString("MM月dd日"));
        return "success";
    }
}
