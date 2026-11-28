package com.xiesn.springboot.hello;

import com.xiesn.springboot.hello.autoconfigure.HelloAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * Boot 3 起步示例启动类。
 * <p>本模块演示：启动流程、自定义自动配置、统一返回 {@code R} 的用法。</p>
 * <p>排除 {@link HelloAutoConfiguration} 的组件扫描，强制走
 * {@code AutoConfiguration.imports}，更接近真实 starter 的加载方式。</p>
 */
@SpringBootApplication
@ComponentScan(
        basePackages = "com.xiesn.springboot.hello",
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = HelloAutoConfiguration.class
        )
)
public class HelloApplication {

    /**
     * 应用入口。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(HelloApplication.class, args);
    }
}
