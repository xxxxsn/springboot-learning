package com.xiesn.springboot.web.restful;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * REST 规范与统一响应演示启动类。
 * <p>本模块聚焦资源命名、HTTP 动词/状态码、{@code Location} 与统一 {@code R}；
 * 深挖校验见 #24，ProblemDetail 见 #25，Jackson 定制见 #26。</p>
 */
@SpringBootApplication
public class WebRestfulApplication {

    /**
     * 应用入口。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(WebRestfulApplication.class, args);
    }
}
