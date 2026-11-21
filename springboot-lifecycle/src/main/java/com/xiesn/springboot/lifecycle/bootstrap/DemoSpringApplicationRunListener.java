package com.xiesn.springboot.lifecycle.bootstrap;

import com.xiesn.springboot.lifecycle.timeline.LifecycleTimeline;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.time.Duration;

/**
 * 自定义 {@link SpringApplicationRunListener}。
 * <p>必须提供 {@code (SpringApplication, String[])} 构造器，并通过
 * {@code META-INF/spring.factories} 注册。比普通 {@code ApplicationListener}
 * 更早，能观察到 starting / environmentPrepared 等阶段。</p>
 */
public class DemoSpringApplicationRunListener implements SpringApplicationRunListener {

    /**
     * Boot 反射创建时调用的构造器。
     *
     * @param application SpringApplication
     * @param args        启动参数
     */
    public DemoSpringApplicationRunListener(SpringApplication application, String[] args) {
        LifecycleTimeline.record(
                "bootstrap",
                "DemoSpringApplicationRunListener",
                "构造完成，args.length=" + (args == null ? 0 : args.length));
    }

    /**
     * 应用开始启动。
     *
     * @param bootstrapContext 引导上下文
     */
    @Override
    public void starting(ConfigurableBootstrapContext bootstrapContext) {
        LifecycleTimeline.record("bootstrap", "RunListener.starting", "SpringApplication 开始启动");
    }

    /**
     * Environment 已准备好。
     *
     * @param bootstrapContext 引导上下文
     * @param environment      环境
     */
    @Override
    public void environmentPrepared(ConfigurableBootstrapContext bootstrapContext,
                                    ConfigurableEnvironment environment) {
        LifecycleTimeline.record(
                "bootstrap",
                "RunListener.environmentPrepared",
                "activeProfiles=" + String.join(",", environment.getActiveProfiles()));
    }

    /**
     * ApplicationContext 已创建但尚未加载。
     *
     * @param context 应用上下文
     */
    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        LifecycleTimeline.record("bootstrap", "RunListener.contextPrepared", "Context 已创建，尚未 refresh");
    }

    /**
     * 配置类 / Bean 定义已加载，即将 refresh。
     *
     * @param context 应用上下文
     */
    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        LifecycleTimeline.record("bootstrap", "RunListener.contextLoaded", "Bean 定义已加载");
    }

    /**
     * 上下文已启动（refresh 完成，Runner 尚未执行）。
     *
     * @param context   应用上下文
     * @param timeTaken 耗时
     */
    @Override
    public void started(ConfigurableApplicationContext context, Duration timeTaken) {
        LifecycleTimeline.record(
                "bootstrap",
                "RunListener.started",
                "refresh 完成，耗时=" + timeTaken.toMillis() + "ms");
    }

    /**
     * 应用已就绪（Runner 执行完毕）。
     *
     * @param context   应用上下文
     * @param timeTaken 耗时
     */
    @Override
    public void ready(ConfigurableApplicationContext context, Duration timeTaken) {
        LifecycleTimeline.record(
                "bootstrap",
                "RunListener.ready",
                "应用就绪，总耗时=" + timeTaken.toMillis() + "ms");
    }

    /**
     * 启动失败。
     *
     * @param context   应用上下文（可能为 null）
     * @param exception 异常
     */
    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        LifecycleTimeline.record(
                "bootstrap",
                "RunListener.failed",
                "启动失败: " + exception.getClass().getSimpleName());
    }
}
