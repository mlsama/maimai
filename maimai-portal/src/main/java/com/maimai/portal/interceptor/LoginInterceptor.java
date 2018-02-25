package com.maimai.portal.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maimai.common.cookie.CookieUtils;
import com.maimai.sso.pojo.User;
import com.maimai.sso.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

/**
 * 描述:拦截器,用户进行订单相关的操作前必须是已登陆状态.
 *
 * @Author:mlsama 2018/2/19 14:07
 */
@Slf4j
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private UserService userService;
    private ObjectMapper objectMapper = new ObjectMapper();
    /**
     * 订单相关的操作必须先登陆
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        log.info("触发订单拦截器.");
        /** 从Cookie中获取登录票据ticket */
        String ticket = CookieUtils.getCookieValue(request,
                CookieUtils.CookieName.MAIMAI_TICKET, false);
        /** 判断ticket是否为空 */
        if (StringUtils.isNoneBlank(ticket)){   //已经登陆
            /** 根据ticket获取用户数据 */
            String userJsonStr = userService.getUserByTicket(ticket);
            /** 把用户json字符串转化成用户对象 */
            User user = objectMapper.readValue(userJsonStr, User.class);
            //把user对象传给OrderController
            request.setAttribute("user",user);
            return true;
        }
        //没有登陆
        //获取当前路径?后面的参数
        String queryParam = request.getQueryString();
        //获取请求路径
        String requestURL = request.getRequestURL().toString();
        if (StringUtils.isNoneBlank(queryParam)){
            requestURL += "?"+ queryParam;
        }
        log.info("重定向路径{}:",requestURL);
        // 跳到登陆页面,把拦截的路径也发过去,登陆成功后进行跳转
        response.sendRedirect("http://sso.maimai.com/login?redirectURL="+ URLEncoder.encode(requestURL,"utf-8"));
        return false;
    }
}
