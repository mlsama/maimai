package com.maimai.sso.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maimai.common.constant.Constant;
import com.maimai.sso.mapper.UserMapper;
import com.maimai.sso.pojo.User;
import com.maimai.sso.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 描述:用户接口实现类
 * @Author:mlsama 2018/1/24 22:54
 */
@Service
@Transactional(readOnly = false)
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 数据验证
     * @param param 验证的数据
     * @param type  数据的类型 1:用户名  2:电话 3:邮箱
     * @return
     */
    @Override
    public Boolean dataVlidate(String param, Integer type) {
        User user = new User();
        if(type == 1){
            user.setUsername(param);
        }
        if(type == 2){
            user.setPhone(param);
        }
        if(type == 3){
            user.setEmail(param);
        }
        return userMapper.countData(user) == 0;
    }


    /**
     * 登陆
     * @param user
     * @return 票据
     */
    @Override
    public String login(User user) {
        try{
            /** 设置用户密码用MD5加密 */
            user.setPassword(DigestUtils.md5Hex(user.getPassword()));
            /** 根据用户名与密码查询用户 */
            User u = userMapper.login(user);
            log.info("用户查询结果为{}:",u);
            /** 判断用户是否为空 */
            if (u != null){
                /** 生成ticket票据 */
                String ticket = DigestUtils.md5Hex(String.valueOf(u.getId()
                        + System.currentTimeMillis()));
                log.info("生成的票据ticket:{}",ticket);
                /** 把用户信息存储到redis中,boundValueOps方法操作string类型 */
                redisTemplate.boundValueOps(Constant.REDIS_TICKET_PREFIX + ticket)
                        .set(objectMapper.writeValueAsString(u));

                /** 设置有效时间为3600秒 */
                redisTemplate.expire(Constant.REDIS_TICKET_PREFIX + ticket,
                        3600, TimeUnit.SECONDS);

                /** 返回 */
                return ticket;
            }
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
        return null;
    }

    /**
     * 根据ticket获取登陆的用户信息
     * @param ticket
     * @return
     */
    @Override
    public String getUserByTicket(String ticket) {
        log.info("根据ticket获取登陆的用户信息ticket:{}",ticket);
        String userInfo = redisTemplate.boundValueOps(Constant.REDIS_TICKET_PREFIX+ticket).get();
        log.info("根据ticket获取登陆的用户信息结果:{}",userInfo);
        //重新设置时间.参数: key   时间    时间单位
        redisTemplate.expire(Constant.REDIS_TICKET_PREFIX + ticket,3600,TimeUnit.SECONDS);
        return userInfo;
    }

    /**
     * 删除redis中的用户信息
     * @param ticket
     */
    @Override
    public void delTicket(String ticket) {
        log.info("删除redis中的用户信息,ticket:{}",ticket);
        redisTemplate.delete(Constant.REDIS_TICKET_PREFIX + ticket);
    }


    /**
     * 注册
     * @param user
     */
    @Override
    public void register(User user) {
        user.setCreated(new Date());
        user.setUpdated(user.getCreated());
        //密码加密
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));
        userMapper.addUser(user);
    }
}
