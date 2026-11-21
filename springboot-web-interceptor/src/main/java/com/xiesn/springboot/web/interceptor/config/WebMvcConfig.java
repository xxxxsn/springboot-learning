package com.xiesn.springboot.web.interceptor.config;

import com.xiesn.springboot.web.interceptor.mvc.AccessLogInterceptor;
import com.xiesn.springboot.web.interceptor.mvc.LoginUserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * MVC 拦截器注册：顺序即 {@code addInterceptor} 次序。
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final LoginUserInterceptor loginUserInterceptor;
    private final AccessLogInterceptor accessLogInterceptor;

    /**
     * 注入拦截器。
     *
     * @param loginUserInterceptor 用户上下文
     * @param accessLogInterceptor 访问日志
     */
    public WebMvcConfig(LoginUserInterceptor loginUserInterceptor,
                        AccessLogInterceptor accessLogInterceptor) {
        this.loginUserInterceptor = loginUserInterceptor;
        this.accessLogInterceptor = accessLogInterceptor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginUserInterceptor)
                .addPathPatterns("/api/interceptor/**")
                .order(1);
        registry.addInterceptor(accessLogInterceptor)
                .addPathPatterns("/api/interceptor/**")
                .order(2);
    }
}
