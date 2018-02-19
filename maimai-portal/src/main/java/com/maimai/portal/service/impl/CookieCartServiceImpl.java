package com.maimai.portal.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maimai.admin.pojo.Item;
import com.maimai.admin.service.ItemService;
import com.maimai.cart.pojo.CartItem;
import com.maimai.common.cookie.CookieUtils;
import com.maimai.portal.service.CookieCartService;

/**
 * Cookie购物车服务接口实现类
 */
@Component
public class CookieCartServiceImpl implements CookieCartService {
	/** 定义ObjectMapper来操作JSON */
	private ObjectMapper objectMapper = new ObjectMapper();
	/** 定义购物车数据存放在Cookie中有效时间 按秒计算 7天 */
	private static final int CART_MAX_AGE = 60 * 60 * 24 * 7;
	@Autowired
	private ItemService itemService;
	
	/**
	 * 添加购物车
	 * @param itemId 商品id
	 * @param buyNum 购买数量
	 * @param request 请求对象
	 * @param response 响应对象
	 */
	public void addCart(Long itemId, Integer buyNum,
			HttpServletRequest request, HttpServletResponse response){
		try{
			/** 从Cookie中获取购物车数据 */
			List<CartItem> cartItems = findCart(request);
			
			/** 定义是否购买过该商品的标识符 */
			boolean isBuy = false;
			
			/** 判断购物车是否存在 */
			if (cartItems != null){
				/** 迭代cartItems判断该商品是否已经购买 */
				for (CartItem cartItem : cartItems){
					if (cartItem.getItemId().equals(itemId)){
						/** 已经购买  */
						cartItem.setNum(cartItem.getNum() + buyNum);
						cartItem.setUpdated(new Date());
						isBuy = true;
						break;
					}
				}
			}else{
				cartItems = new ArrayList<>();
			}
			
			if(!isBuy){ // 代表该商品没有购买过
				/** 根据商品id查询商品 */
				Item item = itemService.selectByPrimaryKey(itemId);
				/** 创建购物车商品对象 */
				CartItem cartItem = new CartItem();
				cartItem.setCreated(new Date());
				cartItem.setItemId(item.getId());
				/** 购物车商品只需要一张商品图片 */
				if (item.getImages() != null && item.getImages().length > 0){
					cartItem.setItemImage(item.getImages()[0]);
				}
				cartItem.setItemPrice(item.getPrice());
				cartItem.setItemTitle(item.getTitle());
				cartItem.setNum(buyNum);
				cartItem.setUpdated(cartItem.getCreated());
				cartItems.add(cartItem);
			}
			/** 把购物车cartItems存储到Cookie中 */
			CookieUtils.setCookie(request, response, 
					CookieUtils.CookieName.MAIMAI_CART,
					objectMapper.writeValueAsString(cartItems), 
					CART_MAX_AGE, true);
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
	/**
	 * 获取购物车
	 * @param request 请求对象
	 * @return List
	 */
	public List<CartItem> findCart(HttpServletRequest request){
		try{
			/** 存入在Cookie中的购物车格式： CookieValue只能存字符串 List<CartItem> 格式的json字符串 */
			/** 通过cookeName获取指定的cookie的值  [{},{}]*/
			String cartItemJsonLists = CookieUtils.getCookieValue(request,
					CookieUtils.CookieName.MAIMAI_CART, true);
			if (StringUtils.isNoneBlank(cartItemJsonLists)){
				/** 把cartItemJsonLists转化成List<CartItem> */
				return objectMapper.readValue(cartItemJsonLists, objectMapper.getTypeFactory()
						.constructCollectionType(List.class, CartItem.class));
			}
			return null;
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
	/**
	 * 修改购物车
	 * @param itemId 商品id
	 * @param buyNum 购买数量
	 * @param request 请求对象
	 * @param response 响应对象
	 */
	public void updateCart(Long itemId, Integer buyNum,
				HttpServletRequest request, HttpServletResponse response){
		try{
			/** 从Cookie中获取购物车数据 */
			List<CartItem> cartItems = findCart(request);
			if (cartItems != null && cartItems.size() > 0){
				for (CartItem cartItem : cartItems){
					/** 判断购物车中的商品 */
					if (cartItem.getItemId().equals(itemId)){
						cartItem.setNum(buyNum);
						cartItem.setUpdated(new Date());
						/** 把购物车cartItems存储到Cookie中 */
						CookieUtils.setCookie(request, response, 
								CookieUtils.CookieName.MAIMAI_CART,
								objectMapper.writeValueAsString(cartItems), 
								CART_MAX_AGE, true);
						break;
					}
				}
			}
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
	/**
	 * 删除购物车
	 * @param itemId 商品id
	 * @param request 请求对象
	 * @param response 响应对象
	 */
	public void deleteCart(Long itemId, HttpServletRequest request,
			HttpServletResponse response){
		try{
			/** 从Cookie中获取购物车数据 */
			List<CartItem> cartItems = findCart(request);
			if (cartItems != null && cartItems.size() > 0){
				Iterator<CartItem> iter = cartItems.iterator();
				while (iter.hasNext()){
					CartItem cartItem = iter.next();
					/** 判断购物车中的商品 */
					if (cartItem.getItemId().equals(itemId)){
						iter.remove();
						break;
					}
				}
			}
			if (cartItems != null && cartItems.size() > 0){
				/** 把购物车cartItems存储到Cookie中 */
				CookieUtils.setCookie(request, response, 
						CookieUtils.CookieName.MAIMAI_CART,
						objectMapper.writeValueAsString(cartItems), 
						CART_MAX_AGE, true);
			}else{
				/** 代表购物车没有商品，把购物车对应的cookie删除 */
				CookieUtils.deleteCookie(request, response, CookieUtils.CookieName.MAIMAI_CART);
			}
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
}