package com.xiesn.springboot.lombok.hutool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Lombok / Hutool / Guava 演示启动类。
 * <p>启动后访问 {@code /api/lombok/overview}、{@code /api/hutool/all}、{@code /api/guava/all}。</p>
 */
@SpringBootApplication
public class LombokHutoolApplication {

    /**
     * 应用入口。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(LombokHutoolApplication.class, args);
    }
}
