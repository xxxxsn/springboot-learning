package com.xiesn.springboot.web.interceptor;

import com.xiesn.springboot.web.interceptor.config.InterceptorDemoProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Filter / Interceptor / Listener 演示启动类。
 * <p>对照 Servlet 过滤器、Spring MVC 拦截器、Servlet 监听器的企业常见职责与执行顺序。
 * {@code @CurrentUser} 参数解析见 #28，跨域见 #29。</p>
 */
@SpringBootApplication
@EnableConfigurationProperties(InterceptorDemoProperties.class)
public class WebInterceptorApplication {

    /**
     * 应用入口。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(WebInterceptorApplication.class, args);
    }
}
