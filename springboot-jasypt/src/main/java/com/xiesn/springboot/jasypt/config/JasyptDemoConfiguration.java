package com.xiesn.springboot.jasypt.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 启用演示配置属性绑定。
 */
@Configuration
@EnableConfigurationProperties(DemoSecretProperties.class)
public class JasyptDemoConfiguration {
}
