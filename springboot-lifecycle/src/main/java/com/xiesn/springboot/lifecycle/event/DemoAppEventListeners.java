package com.xiesn.springboot.lifecycle.event;

import com.xiesn.springboot.lifecycle.timeline.LifecycleTimeline;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 应用事件监听示例。
 * <p>用 {@code @EventListener} 覆盖启动与关闭阶段最常见的几个事件。</p>
 */
@Component
public class DemoAppEventListeners {

    /**
     * Context 完成 refresh（可能触发多次，如父子上下文）。
     *
     * @param event 刷新事件
     */
    @EventListener
    public void onContextRefreshed(ContextRefreshedEvent event) {
        LifecycleTimeline.record(
                "event",
                "ContextRefreshedEvent",
                "context=" + event.getApplicationContext().getId());
    }

    /**
     * 应用已启动（Runner 尚未全部跑完的分界在 Boot 文档中有说明；此处记时间线即可）。
     *
     * @param event 启动事件
     */
    @EventListener
    public void onApplicationStarted(ApplicationStartedEvent event) {
        LifecycleTimeline.record(
                "event",
                "ApplicationStartedEvent",
                "source=" + event.getSpringApplication().getMainApplicationClass().getSimpleName());
    }

    /**
     * 应用就绪，可对外服务。
     *
     * @param event 就绪事件
     */
    @EventListener
    public void onApplicationReady(ApplicationReadyEvent event) {
        LifecycleTimeline.record(
                "event",
                "ApplicationReadyEvent",
                "应用可对外提供服务");
    }

    /**
     * 上下文关闭（优雅停机流程中会触发）。
     *
     * @param event 关闭事件
     */
    @EventListener
    public void onContextClosed(ContextClosedEvent event) {
        LifecycleTimeline.record(
                "shutdown",
                "ContextClosedEvent",
                "ApplicationContext 正在关闭");
    }
}
