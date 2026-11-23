package com.xiesn.springboot.web.resolver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 参数解析器与返回值处理器演示。
 * <p>登录用户从请求头进 Filter，再由 {@code @CurrentUser} 注入方法参数。跨域见 #29。</p>
 */
@SpringBootApplication
public class WebResolverApplication {

    /**
     * 应用入口。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(WebResolverApplication.class, args);
    }
}
