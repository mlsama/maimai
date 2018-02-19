package com.maimai.cart;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * 描述:启动类
 *
 * @Author:mlsama 2018/2/12 16:38
 */
@ImportResource(locations="classpath:applicationContext-dubbo.xml")
@SpringBootApplication
public class CartServiceApplication {

    public static void main(String[] args) {
        /** 创建SpringApplication应用对象 */
        SpringApplication springApplication = new SpringApplication(CartServiceApplication.class);
        /** 设置横幅模式 */
        springApplication.setBannerMode(Mode.OFF);
        /** 运行 */
        springApplication.run(args);
    }
}
