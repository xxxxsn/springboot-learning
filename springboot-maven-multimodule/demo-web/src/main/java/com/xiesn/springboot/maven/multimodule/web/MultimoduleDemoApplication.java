package com.xiesn.springboot.maven.multimodule.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 多模块案例启动类。
 * <p>只放在 web 模块；api / service 是 jar，不能也不应各自启动。</p>
 * <p>{@code scanBasePackages} 扫到 service 包，才能发现 {@code ProductFacadeImpl}。</p>
 */
@SpringBootApplication(scanBasePackages = {
        "com.xiesn.springboot.maven.multimodule.web",
        "com.xiesn.springboot.maven.multimodule.service"
})
public class MultimoduleDemoApplication {

    /**
     * 应用入口。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(MultimoduleDemoApplication.class, args);
    }
}
