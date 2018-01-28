package com.maimai.sso.controller;

import com.maimai.common.constant.Constant;
import com.maimai.common.cookie.CookieUtils;
import com.maimai.sso.pojo.User;
import com.maimai.sso.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述:用户控制类
 *
 * @Author:mlsama 2018/1/24 23:14
 */
@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 注册数据验证(是否已经使用)
     * @param param 验证的数据
     * @param type  数据的类型 1:用户名  2:电话 3:邮箱
     * @return
     */
    @GetMapping("/check/{param}/{type}")
    public ResponseEntity<String> dataVlidate(@PathVariable("param") String param,
                                              @PathVariable("type") Integer type){
        log.info("注册数据验证请求参数{},{}:",param,type);
        try {
            if (StringUtils.isNoneBlank(param) && type != null
                    && type >= 1 && type <= 3){
                Boolean isPass = userService.dataVlidate(param,type);
                return ResponseEntity.ok(isPass.toString());
            }
            //错误请求
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }catch (Exception e){
            //异常
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/register")
    @ResponseBody
    public Map<String,Object> register(User user){
        log.info("注册请求参数{}:",user);
        Map<String,Object> map = new HashMap<>();
        try {
            userService.register(user);
            map.put("status",200);
        }catch (Exception e){
            map.put("status",500);
        }
        return map;
    }
    /** 登录 */
    @PostMapping("/login")
    public @ResponseBody Map<String, Object> login(User user,
                                                   HttpServletResponse response,
                                                   HttpServletRequest request){
        log.info("登陆请求参数{}:",user);
        /** 创建Map集合封装响应数据 */
        Map<String, Object> data = new HashMap<>();
        try{
            /** 用户登录后返回一个票据字符串 */
            String ticket = userService.login(user);
            data.put("status", 500);
            if (StringUtils.isNoneBlank(ticket)){
                /** 把该ticket存入cookie */
                CookieUtils.setCookie(request, response,
                        CookieUtils.CookieName.MAIMAI_TICKET,
                        ticket, 3600, false);
                /** 设置状态码 */
                data.put("status", 200);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        log.info("登陆返回的数据是{}:",data);
        return data;
    }

    /**
     * 根据ticket获取登陆的用户信息.有可能是跨域,也可能是不跨域
     * 跨域: 会传回调函数名过来(默认是ajax的success匿名方法名)
     * @param ticket 票据
     * @param callback 回调方法名
     * @return ResponseEntity<String>
     */
    @GetMapping("/{ticket}")
    public ResponseEntity<String> getUserByTicket(@PathVariable("ticket") String ticket,
                                                @RequestParam(value = "callback",
                                                required = false) String callback){
        try {
            //获取登陆用户信息
            String data = userService.getUserByTicket(ticket);
            if (StringUtils.isNoneBlank(data)){
                //定义StringBuilder封装数据
                StringBuilder sb = new StringBuilder();
                if (StringUtils.isNoneBlank(callback)){
                    //注意;
                    sb.append(callback + "("+data+");");
                }else {
                    sb.append(data);
                }
                return ResponseEntity.ok(sb.toString());
            }else {
                return ResponseEntity.ok(null);
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 退出应该清空cookie保存的ticket值和redis中的用户信息；并且重定向到当前页面
     * @param redirectURL
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/logout")
    public String logout(@RequestParam(value = "redirectURL",
                         defaultValue = "http://www.maimai.com/")String redirectURL,
                         HttpServletRequest request,
                         HttpServletResponse response){
        try {
            //获取票据
            String ticket = CookieUtils.getCookieValue(request, CookieUtils.CookieName.MAIMAI_TICKET,false);
            //删除redis中的用户信息
            userService.delTicket(ticket);
            //删除cookie
            CookieUtils.deleteCookie(request,response,CookieUtils.CookieName.MAIMAI_TICKET);
            return "redirect:" + redirectURL;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
