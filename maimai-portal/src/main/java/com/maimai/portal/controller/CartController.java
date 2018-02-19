package com.maimai.portal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maimai.admin.pojo.Item;
import com.maimai.admin.service.ItemService;
import com.maimai.cart.pojo.CartItem;
import com.maimai.cart.service.CartService;
import com.maimai.common.cookie.CookieUtils;
import com.maimai.portal.service.CookieCartService;
import com.maimai.sso.pojo.User;
import com.maimai.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 描述:购物车控制类.没有操作数据库，不需要事务
 *
 * @Author:mlsama 2018/2/12 16:57
 */
@Controller
@RequestMapping("/cart")
public class CartController {
    /** 注入用户服务 */
    @Autowired
    private UserService userService;
    /** 注入商品服务 */
    @Autowired
    private ItemService itemService;
    /** 注入购物车服务:已登录 */
    @Autowired
    private CartService cartService;
    /** 注入购物车服务:未登陆 */
    @Autowired
    private CookieCartService cookieCartService;
    /** 定义ObjectMapper操作json */
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 加入购物车.注意：xxx.html不是html页面而是一个请求
     * 在web.xml配置了*.html
     * @param itemId 商品id
     * @param num   商品数量
     * @param request
     * @param response
     * @return  重定向到显示购物车商品的处理器
     * @throws Exception
     */
    @GetMapping("/{itemId}/{num}")
    public String addCart(@PathVariable("itemId")Long itemId,
                          @PathVariable("num")Integer num,
                          HttpServletRequest request,
                          HttpServletResponse response) throws Exception{
        //从cookie获取用户登陆票据
        String ticket = CookieUtils.getCookieValue(request, CookieUtils.CookieName.MAIMAI_TICKET, false);
        //已登录,把购物车中的数据存入Redis数据库中
        if (StringUtils.isNoneBlank(ticket)){
            //根据ticket获取redis中json格式的用户信息
            String userJson = userService.getUserByTicket(ticket);
            //转换为对象
            User user = objectMapper.readValue(userJson, User.class);
            //获取商品
            Item item = itemService.selectByPrimaryKey(itemId);
            //把商品添加到购物车:用户id作为用户购物车的标识的一部分(唯一区分)
            cartService.add2Cart(user.getId(),item,num);
        }else{  //没有登陆
            //把商品添加到cookie
            cookieCartService.addCart(itemId,num,request,response);
        }
        //重定向到显示购物车商品的处理器,showCart.html是一个请求
        return "redirect:/cart/showCart.html";
    }

    /**
     * 显示购物车
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/showCart")
    public String showCart(HttpServletRequest request, Model model){
        try {
            /** 从Cookie中获取登录票据ticket */
            String ticket = CookieUtils.getCookieValue(request,
                    CookieUtils.CookieName.MAIMAI_TICKET, false);
            //定义购物车商品列表
            List<CartItem> cartList = null;
            /** 判断ticket是否为空 */
            if (StringUtils.isNoneBlank(ticket)){
                /** ##### 用户已登录，需要从Redis中获取购物车数据 #### */
                /** 根据ticket获取用户数据 */
                String userJsonStr = userService.getUserByTicket(ticket);
                /** 把用户json字符串转化成用户对象 */
                User user = objectMapper.readValue(userJsonStr, User.class);
                cartList = cartService.findCartBuUserId(user.getId());
            }else{
                /** ### 用户没有登录，需要从Cookie中获取购物车数据 ### */
                cartList = cookieCartService.findCart(request);
            }
            model.addAttribute("cart",cartList);
            return "cart";
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 修改购物车商品数量
     * @param itemId 商品id
     * @param num   数量
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/update/{itemId}/{num}")
    public ResponseEntity<Void> updateCart(@PathVariable("itemId")Long itemId,
                                           @PathVariable("num")Integer num,
                                           HttpServletRequest request,
                                           HttpServletResponse response){
        try{
            /** 从Cookie中获取登录票据ticket */
            String ticket = CookieUtils.getCookieValue(request,
                    CookieUtils.CookieName.MAIMAI_TICKET, false);
            /** 判断ticket是否为空 */
            if (StringUtils.isNoneBlank(ticket)){
                /** ##### 用户已登录，需要从Redis中获取购物车数据 #### */
                /** 根据ticket获取用户数据 */
                String userJsonStr = userService.getUserByTicket(ticket);
                /** 把用户json字符串转化成用户对象 */
                User user = objectMapper.readValue(userJsonStr, User.class);
                /** 更新购物车中的数据 */
                cartService.updateCartItemNum(user.getId(), itemId, num);
            }else{
                /** ### 用户没有登录，需要从Cookie中修改购物车数据 ### */
                cookieCartService.updateCart(itemId,num,request,response);
            }
            return ResponseEntity.ok().build();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    /**
     * 删除购物车
     */
    @RequestMapping("/delete/{itemId}")
    public String deleteCart(@PathVariable("itemId")Long itemId,
                             HttpServletRequest request,
                             HttpServletResponse response){
        try{
            /** 从Cookie中获取登录票据ticket */
            String ticket = CookieUtils.getCookieValue(request,
                    CookieUtils.CookieName.MAIMAI_TICKET, false);
            /** 判断ticket是否为空 */
            if (StringUtils.isNoneBlank(ticket)){
                /** ##### 用户已登录，需要从Redis中删除购物车数据 #### */
                /** 根据ticket获取用户数据 */
                String userJsonStr = userService.getUserByTicket(ticket);
                /** 把用户json字符串转化成用户对象 */
                User user = objectMapper.readValue(userJsonStr, User.class);
                /** 删除购物车中的数据 */
                cartService.deleteCartByUserId(user.getId(), itemId);
            }else{
                /** ### 用户没有登录，需要从Cookie中删除购物车数据 ### */
                cookieCartService.deleteCart(itemId,request,response);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        //重定向到显示购物车商品的处理器,showCart.html是一个请求
        return "redirect:/cart/showCart.html";
    }


}
