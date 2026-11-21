package com.xiesn.springboot.logback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Logback MDC / JSON / 分文件演示启动类。
 * <p>异步对比见 #19 log4j2；本模块聚焦企业最常用的 Logback 落地形态。</p>
 */
@SpringBootApplication
public class LogbackApplication {

    /**
     * 应用入口。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(LogbackApplication.class, args);
    }
}