package com.xiesn.springboot.lifecycle.runner;

import com.xiesn.springboot.lifecycle.timeline.LifecycleTimeline;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * {@link ApplicationRunner} 示例。
 * <p>{@code @Order(1)} 比 {@link DemoCommandLineRunner} 更小，因此先执行。</p>
 */
@Component
@Order(1)
public class DemoApplicationRunner implements ApplicationRunner {

    private final Environment environment;

    /**
     * 注入 Environment，演示 Runner 里已可读取配置。
     *
     * @param environment 环境
     */
    public DemoApplicationRunner(Environment environment) {
        this.environment = environment;
    }

    /**
     * 执行应用级引导逻辑。
     *
     * @param args 解析后的启动参数
     */
    @Override
    public void run(ApplicationArguments args) {
        String flag = environment.getProperty("lifecycle.demo.initializer-ready", "false");
        LifecycleTimeline.record(
                "runner",
                "ApplicationRunner(@Order=1)",
                "optionNames=" + args.getOptionNames()
                        + ", initializer-ready=" + flag);
    }
}
