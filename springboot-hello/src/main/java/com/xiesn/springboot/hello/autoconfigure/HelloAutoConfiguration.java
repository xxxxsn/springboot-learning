package com.xiesn.springboot.hello.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Hello 自定义自动配置。
 * <p>Boot 3 不再使用 {@code spring.factories} 注册自动配置，
 * 而是写入 {@code META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports}。</p>
 */
@AutoConfiguration
@EnableConfigurationProperties(HelloProperties.class)
@ConditionalOnProperty(prefix = "demo.hello", name = "enabled", havingValue = "true", matchIfMissing = true)
public class HelloAutoConfiguration {

    /**
     * 注册 HelloService；若业务侧已手动声明同类型 Bean 则跳过，方便覆盖。
     *
     * @param properties 配置属性
     * @return Hello 服务
     */
    @Bean
    @ConditionalOnMissingBean
    public HelloService helloService(HelloProperties properties) {
        return new HelloService(properties);
    }
}
