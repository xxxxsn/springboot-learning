package com.xiesn.springboot.lifecycle.bootstrap;

import com.xiesn.springboot.lifecycle.timeline.LifecycleTimeline;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.MapPropertySource;

import java.util.Map;

/**
 * 自定义 {@link ApplicationContextInitializer}。
 * <p>在 Context {@code refresh} 之前执行，适合放入早期开关、诊断属性。
 * 通过 {@code META-INF/spring.factories} 注册。</p>
 */
public class DemoApplicationContextInitializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {

    /**
     * 初始化上下文：写入演示属性并记入时间线。
     *
     * @param applicationContext 应用上下文
     */
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        applicationContext.getEnvironment().getPropertySources().addFirst(
                new MapPropertySource(
                        "lifecycleDemoInitializer",
                        Map.of("lifecycle.demo.initializer-ready", "true")));
        LifecycleTimeline.record(
                "bootstrap",
                "ApplicationContextInitializer",
                "写入属性 lifecycle.demo.initializer-ready=true（refresh 前）");
    }

    /**
     * 排序值，数值越小越靠前。
     *
     * @return 顺序
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }
}
