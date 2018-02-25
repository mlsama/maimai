package com.maimai.cart.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maimai.admin.pojo.Item;
import com.maimai.cart.pojo.CartItem;
import com.maimai.cart.service.CartService;
import com.maimai.common.cookie.CookieUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 描述:购物车实现接口
 *
 * @Author:mlsama 2018/2/12 16:42
 */
@Service
@Slf4j
public class CartServiceImpl implements CartService {
    //注入redisTemplate
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    //定义ObjectMapper操作json
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 一个用户一个购物车
     * 添加商品到购物车:登陆状态下,购物车保存在redis,否则购物车保存在cookie
     * 先取出购物车
     *      如果有,更新或者添加商品
     * 最后更新购物车,如果没有购物车的话,会生成.
     * @param id 用户id 用于标识用户的购物车
     * @param item  商品
     * @param num   商品数量
     */
    @Override
    public void add2Cart(Long id, Item item, Integer num) {
        try {
            log.info("加入购物车,用户id:{},商品:{},商品数量:{}",id,item,num);
            //定义购物车的名称
            String myCart = CookieUtils.CookieName.MAIMAI_CART + id;
            //定义商品的json格式的字符串
            String itemJsonString = null;
            //从redis中获取购物车
            if (redisTemplate.hasKey(myCart)) {
                //购物车如果存在,根据商品id获取商品对象json字符串
                itemJsonString = (String) redisTemplate.boundHashOps(myCart).get(item.getId().toString());
            }
            //定义商品对象
            CartItem cartItem = null;
            //商品存在
            if (itemJsonString != null){
                //转换成CartItem对象,数量叠加即可
                cartItem = objectMapper.readValue(itemJsonString, CartItem.class);
                cartItem.setNum(cartItem.getNum() + num);
                cartItem.setUpdated(new Date());
            }else { //商品不存在
                //设置购物车商品对象
                cartItem = new CartItem();
                cartItem.setItemId(item.getId());
                cartItem.setNum(num);
                cartItem.setCreated(new Date());
                cartItem.setUpdated(cartItem.getCreated());
                //有多个图片,取一个
                if (item.getImages() != null && item.getImages().length > 0){
                    cartItem.setItemImage(item.getImages()[0]);
                }
                cartItem.setItemPrice(item.getPrice());
                cartItem.setUserId(id);
                cartItem.setItemTitle(item.getTitle());
            }
            /**
             * 把商品保存到redis:商品id为key,cartItem对象的json字符串为value
             * 如果没有购物车的话,会创建一个购物车并保存cartItem数据.
             */
            redisTemplate.boundHashOps(myCart).put(item.getId().toString(),
                                        objectMapper.writeValueAsString(cartItem));
            //设置生成时间
            redisTemplate.boundHashOps(myCart).expire(30, TimeUnit.DAYS);

        }catch (Exception e){
            log.info("商品加入购物车异常:{}",e);
        }
    }

    /**
     * 修改购物车商品数量
     * @param userId 用户id
     * @param itemId 商品id
     * @param num 购买数量
     */
    @Override
    public void updateCartItemNum(Long userId, Long itemId, Integer num) {
        try{
            log.info("修改购物车,用户id:{},商品id:{},商品数量:{}",userId,itemId,num);
            //定义购物车的名称
            String myCart = CookieUtils.CookieName.MAIMAI_CART + userId;
            /** 从购物车中获取指定的购买商品 */
            String cartItemJsonStr = redisTemplate.boundHashOps(myCart)
                    .get(itemId.toString()).toString();
            /** 判断 */
            if (StringUtils.isNoneBlank(cartItemJsonStr)){
                /** 转换对象 */
                CartItem cartItem = objectMapper.readValue(cartItemJsonStr,
                        CartItem.class);
                cartItem.setNum(num);
                cartItem.setUpdated(new Date());
                /** 同步redis */
                redisTemplate.boundHashOps(myCart)
                        .put(itemId.toString(),
                                objectMapper.writeValueAsString(cartItem));
            }
        }catch(Exception ex){
            log.info("修改购物车异常");
        }
    }

    /**
     * 删除购物车:根据用户id找到购物车,根据商品id找到对应的商品
     * 如果购物车里没有商品,会自动删除购物车的.
     * @param userId
     * @param itemId
     */
    @Override
    public void deleteCartByUserId(Long userId, Long itemId) {
        try {
            log.info("删除购物车,用户id:{},商品id:{}",userId,itemId);
            //定义购物车的名称
            String myCart = CookieUtils.CookieName.MAIMAI_CART + userId;
            //删除对应的商品
            redisTemplate.boundHashOps(myCart).delete(itemId);

        }catch (Exception e){
            log.info("删除购物车异常");
        }
    }

    /**
     * 根据用户id获取用户购物车
     * 购物车模型:
     *      购物车名称
     *          商品id    商品信息
     *          ...
     * @param userId    用户id
     * @return  购物车数据
     */
    @Override
    public List<CartItem> findCartBuUserId(Long userId) {
        try {
            log.info("查询用户购物车商品id:{}",userId);
            //定义购物车的名称
            String myCart = CookieUtils.CookieName.MAIMAI_CART + userId;
            /** 获取购物车中所有的商品 */
            Map<Object,Object> cartMap = redisTemplate.
                    boundHashOps(myCart).entries();
            /** 定义List集合封装最终购物车数据 */
            List<CartItem> cart = new ArrayList<>();
            if (cartMap != null && cartMap.size() > 0){
                /** 判断购物车 */
                for (Object obj : cartMap.values()){
                    /** 把cartItemJson转化成CartItem */
                    CartItem cartItem = objectMapper
                            .readValue(obj.toString(), CartItem.class);
                    cart.add(cartItem);
                }
            }
            return cart;
        }catch (Exception e){
            log.info("根据用户id获取用户购物车异常.");
            throw new RuntimeException(e);
        }
    }

    /**
     * 合并购物车
     * @param cartItemJsonLists cookie中的购物车数据(json格式的字符串)
     * @param id    用户id
     */
    @Override
    public void magreCart(String cartItemJsonLists, Long id) {
        try{
            log.info("合并购物车{},{}",cartItemJsonLists,id);
            /** 把json字符串，转化成List集合 */
            List<CartItem> carts = objectMapper.readValue(cartItemJsonLists,
                    objectMapper.getTypeFactory()
                            .constructCollectionType(List.class, CartItem.class));
            //逐个合并
            for (CartItem cartItem : carts){
                magreCartByUserId(id, cartItem);
            }
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /**
     * 清空购物车:逻辑有问题
     * @param userId
     */
    @Override
    public void clearCart(Long userId) {
        try{
            /** 定义Redis数据库中的购物车的key */
            String mycart = CookieUtils.CookieName.MAIMAI_CART + userId;
            redisTemplate.delete(mycart);
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /**
     * 合并一个商品
     * @param id 用户id
     * @param cookieCartItem    cookie中的一个商品
     */
    private void magreCartByUserId(Long id, CartItem cookieCartItem) {
        try{
            /** 定义Redis数据库中的购物车的key */
            String mycart = CookieUtils.CookieName.MAIMAI_CART + id;
            //获取cookie中的商品的id
            String field = cookieCartItem.getItemId().toString();
            //用cookie中的商品的id去redis中获取商品
            Object cartItemJsonStr =
                    redisTemplate.boundHashOps(mycart).get(field);
            /** 定义购物车中的商品对象 */
            CartItem cartItem = null;
            //如果redis中有这个商品
            if (cartItemJsonStr != null){
                /**数量叠加*/
                /** 把购物车的中商品转化成CartItem对象 */
                cartItem = objectMapper.readValue(cartItemJsonStr.toString(), CartItem.class);
                /** 设置购买数量 */
                cartItem.setNum(cartItem.getNum() + cookieCartItem.getNum());
                /** 设置修改时间 */
                cartItem.setUpdated(new Date());
            }else{
                /** #### 购物车中没有该商品，新增 ####*/
                cartItem = cookieCartItem;
                /** 设置修改时间 */
                cartItem.setUpdated(new Date());
                /** 设置用户编号:必须,因为cookie中的CartItem是没有用户id的 */
                cartItem.setUserId(id);
            }
            /** 添加商品到购物车(Redis数据库中) */
            redisTemplate.boundHashOps(mycart)
                    .put(field, objectMapper.writeValueAsString(cartItem));
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

}
