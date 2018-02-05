package com.maimai.search;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * 描述:启动类
 *
 * @Author:mlsama 2018/2/1 21:09
 */
@ImportResource(locations="classpath:applicationContext-dubbo.xml")
@SpringBootApplication  //默认扫描本类所在的包下的所有类
public class SearchServiceApplication {

    public static void main(String[] args) {
        /** 创建SpringApplication应用对象 */
        SpringApplication springApplication = new SpringApplication(SearchServiceApplication.class);
        /** 设置横幅模式 */
        springApplication.setBannerMode(Mode.OFF);
        /** 运行 */
        springApplication.run(args);
    }
}