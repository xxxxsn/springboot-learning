package com.xiesn.springboot.common.config;

import com.xiesn.springboot.common.exception.GlobalExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * common 模块自动配置入口。
 * <p>通过 {@code AutoConfiguration.imports} 注册，业务模块只需依赖本 jar，
 * 无需再写 {@code @ComponentScan} 才能启用全局异常处理。</p>
 */
@AutoConfiguration
@Import(GlobalExceptionHandler.class)
public class CommonAutoConfiguration {
}
