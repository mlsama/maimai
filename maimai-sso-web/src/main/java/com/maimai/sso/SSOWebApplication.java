package com.maimai.sso;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * 描述:启动类
 *
 * @Author:mlsama 2018/1/23 23:05
 */
@ImportResource(locations="classpath:maimai-sso-web-dubbo.xml")
@SpringBootApplication(scanBasePackages={"com.maimai.sso"})
public class SSOWebApplication {

    public static void main(String[] args) {
        /** 创建SpringApplication应用对象 */
        SpringApplication springApplication = new SpringApplication(SSOWebApplication.class);
        /** 设置横幅模式 */
        springApplication.setBannerMode(Mode.OFF);
        /** 运行 */
        springApplication.run(args);
    }
}