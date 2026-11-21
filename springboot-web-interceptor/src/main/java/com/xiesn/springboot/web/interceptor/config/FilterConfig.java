package com.xiesn.springboot.web.interceptor.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiesn.springboot.web.interceptor.filter.DemoAuthFilter;
import com.xiesn.springboot.web.interceptor.filter.TraceIdFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * Filter 注册：用 {@link FilterRegistrationBean} 精确控制顺序与 URL 模式（企业常见写法）。
 */
@Configuration
public class FilterConfig {

    /**
     * Trace Filter：全局，最早。
     *
     * @return 注册 Bean
     */
    @Bean
    public FilterRegistrationBean<TraceIdFilter> traceIdFilterRegistration() {
        FilterRegistrationBean<TraceIdFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new TraceIdFilter());
        bean.addUrlPatterns("/*");
        bean.setName("traceIdFilter");
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE + 10);
        return bean;
    }

    /**
     * 鉴权 Filter：仅 secure 路径。
     *
     * @param properties   配置
     * @param objectMapper JSON
     * @return 注册 Bean
     */
    @Bean
    public FilterRegistrationBean<DemoAuthFilter> demoAuthFilterRegistration(
            InterceptorDemoProperties properties,
            ObjectMapper objectMapper) {
        FilterRegistrationBean<DemoAuthFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new DemoAuthFilter(properties, objectMapper));
        bean.addUrlPatterns("/api/interceptor/secure/*");
        bean.setName("demoAuthFilter");
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE + 20);
        return bean;
    }
}
