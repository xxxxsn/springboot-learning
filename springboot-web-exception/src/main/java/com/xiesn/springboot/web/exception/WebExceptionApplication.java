package com.xiesn.springboot.web.exception;

import com.xiesn.springboot.web.exception.support.ExceptionProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 企业级全局异常演示启动类。
 * <p>本模块提供可下沉到公司 common/framework 的异常封装样板：
 * 错误码→HTTP 映射、traceId、字段错误列表、信封 / ProblemDetail 双模式。
 * 不改动 {@code springboot-common} 默认处理器，避免牵动 #23/#24。</p>
 */
@SpringBootApplication
@EnableConfigurationProperties(ExceptionProperties.class)
public class WebExceptionApplication {

    /**
     * 应用入口。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(WebExceptionApplication.class, args);
    }
}
