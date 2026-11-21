package com.xiesn.springboot.web.validation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 参数校验演示启动类。
 * <p>本模块按企业常见封装：自定义注解、分组、嵌套校验、错误明细进 {@code R}；
 * ProblemDetail 见 #25，校验文案 i18n 见 #31。</p>
 */
@SpringBootApplication
public class WebValidationApplication {

    /**
     * 应用入口。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(WebValidationApplication.class, args);
    }
}
