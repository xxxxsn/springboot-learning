package com.xiesn.springboot.log4j2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Log4j2 异步日志演示启动类。
 * <p>本模块排除 Logback，使用 {@code spring-boot-starter-log4j2}；
 * 深度 MDC/JSON/分文件留给 #18 logback，这里突出异步对比。</p>
 */
@SpringBootApplication
public class Log4j2Application {

    /**
     * 应用入口。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(Log4j2Application.class, args);
    }
}