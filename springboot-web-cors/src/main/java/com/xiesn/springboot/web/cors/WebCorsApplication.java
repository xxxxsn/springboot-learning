package com.xiesn.springboot.web.cors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 跨域演示启动类。
 * <p>浏览器是否放行由响应头决定。本模块用 MockMvc 核对服务端写出的 CORS 头。</p>
 */
@SpringBootApplication
public class WebCorsApplication {

    /**
     * 应用入口。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(WebCorsApplication.class, args);
    }
}
