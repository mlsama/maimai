package com.maimai.item;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * 描述:启动类
 *
 * @Author:mlsama 2018/2/6 22:48
 */
@ImportResource(locations="classpath:maimai-item-web-dubbo.xml")
@SpringBootApplication
public class ItemWebApplication {

    public static void main(String[] args) {
        /** 创建SpringApplication应用对象 */
        SpringApplication springApplication = new SpringApplication(ItemWebApplication.class);
        /** 设置横幅模式 */
        springApplication.setBannerMode(Mode.OFF);
        /** 运行 */
        springApplication.run(args);
    }
}