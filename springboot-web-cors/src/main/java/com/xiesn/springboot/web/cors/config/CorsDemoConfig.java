package com.xiesn.springboot.web.cors.config;

import com.xiesn.springboot.web.cors.support.CorsOrigins;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 全局跨域只覆盖 {@code /api/cors/**}。
 * <p>{@code allowCredentials(true)} 时不能 {@code allowedOrigins("*")}，Spring 会直接拒绝这个配置。</p>
 */
@Configuration
public class CorsDemoConfig implements WebMvcConfigurer {

    /**
     * {@inheritDoc}
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/cors/**")
                .allowedOrigins(CorsOrigins.ADMIN)
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .exposedHeaders(CorsOrigins.EXPOSED_HEADER)
                .allowCredentials(true)
                .maxAge(CorsOrigins.MAX_AGE_SECONDS);
    }
}
