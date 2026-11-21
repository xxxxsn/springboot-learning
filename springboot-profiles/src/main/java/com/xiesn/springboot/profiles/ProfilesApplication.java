package com.xiesn.springboot.profiles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 多环境 Profile 演示启动类。
 * <p>默认 active=dev；可用 --spring.profiles.active=production 验证 group 展开。</p>
 */
@SpringBootApplication
public class ProfilesApplication {

    /**
     * 应用入口。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(ProfilesApplication.class, args);
    }
}