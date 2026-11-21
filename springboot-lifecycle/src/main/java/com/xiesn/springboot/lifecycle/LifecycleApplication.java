package com.xiesn.springboot.lifecycle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 生命周期全覆盖演示启动类。
 * <p>启动后访问 {@code /api/lifecycle/timeline} 查看各钩子执行顺序。</p>
 */
@SpringBootApplication
public class LifecycleApplication {

    /**
     * 应用入口。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(LifecycleApplication.class, args);
    }
}
