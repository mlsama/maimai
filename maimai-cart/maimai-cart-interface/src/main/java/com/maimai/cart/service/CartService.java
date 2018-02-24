package com.maimai.cart.service;

import com.maimai.admin.pojo.Item;
import com.maimai.cart.pojo.CartItem;

import java.util.List;

/**
 * 描述:购物车服务接口
 *
 * @Author:mlsama 2018/2/12 16:41
 */
public interface CartService {
    void add2Cart(Long id, Item item, Integer num);

    void updateCartItemNum(Long userId, Long itemId, Integer num);

    void deleteCartByUserId(Long id, Long itemId);

    List<CartItem> findCartBuUserId(Long id);

    void magreCart(String cartItemJsonLists, Long id);

    void clearCart(Long id);
}
