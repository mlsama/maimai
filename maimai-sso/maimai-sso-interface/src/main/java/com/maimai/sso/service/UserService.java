package com.maimai.sso.service;

import com.maimai.sso.pojo.User;
/**
 * 描述:用户接口
 *
 * @Author:mlsama 2018/1/24 23:09
 */
public interface UserService {
    Boolean dataVlidate(String param, Integer type);

    void register(User user);

    String login(User user);

    String getUserByTicket(String ticket);

    void delTicket(String ticket);
}
