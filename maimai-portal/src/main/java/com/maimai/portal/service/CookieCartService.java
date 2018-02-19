package com.maimai.portal.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.maimai.cart.pojo.CartItem;

/**
 * Cookie购物车服务接口
 */
public interface CookieCartService {
	/**
	 * 添加购物车
	 * @param itemId 商品id
	 * @param buyNum 购买数量
	 * @param request 请求对象
	 * @param response 响应对象
	 */
	void addCart(Long itemId, Integer buyNum, HttpServletRequest request, HttpServletResponse response);
	/**
	 * 获取购物车
	 * @param request 请求对象
	 * @return List
	 */
	List<CartItem> findCart(HttpServletRequest request);
	/**
	 * 修改购物车
	 * @param itemId 商品id
	 * @param buyNum 购买数量
	 * @param request 请求对象
	 * @param response 响应对象
	 */
	void updateCart(Long itemId, Integer buyNum, HttpServletRequest request, HttpServletResponse response);
	/**
	 * 删除购物车
	 * @param itemId 商品id
	 * @param request 请求对象
	 * @param response 响应对象
	 */
	void deleteCart(Long itemId, HttpServletRequest request, HttpServletResponse response);

}
