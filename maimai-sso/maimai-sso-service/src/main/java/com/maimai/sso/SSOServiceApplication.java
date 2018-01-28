package com.maimai.sso;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * 描述:单点登录服务层启动类
 *
 * @Author:mlsama 2018/1/23 20:22
 */
@ImportResource(locations={"classpath:applicationContext-dubbo.xml"}) // 加载指定的配置文件
@SpringBootApplication(scanBasePackages={"com.maimai.sso"})
public class SSOServiceApplication {

    public static void main(String[] args) {
        /** 创建SpringApplication对象 */
        SpringApplication springApplication =
                new SpringApplication(SSOServiceApplication.class);
        /** 设置横幅关闭 */
        springApplication.setBannerMode(Mode.OFF);
        /** 运行 */
        springApplication.run(args);
    }
}
