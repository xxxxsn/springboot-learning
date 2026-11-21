package com.xiesn.springboot.lifecycle.runner;

import com.xiesn.springboot.lifecycle.timeline.LifecycleTimeline;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * {@link CommandLineRunner} 示例。
 * <p>参数是原始 {@code String[]}；适合解析命令行。与 {@link org.springframework.boot.ApplicationRunner}
 * 都在容器就绪后、{@code RunListener.ready} 之前执行。</p>
 */
@Component
@Order(2)
public class DemoCommandLineRunner implements CommandLineRunner {

    /**
     * 执行命令行引导逻辑。
     *
     * @param args 原始启动参数
     */
    @Override
    public void run(String... args) {
        LifecycleTimeline.record(
                "runner",
                "CommandLineRunner(@Order=2)",
                "收到原始 args，length=" + args.length);
    }
}
