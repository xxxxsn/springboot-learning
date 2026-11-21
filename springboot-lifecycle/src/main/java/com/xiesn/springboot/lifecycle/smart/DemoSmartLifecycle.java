package com.xiesn.springboot.lifecycle.smart;

import com.xiesn.springboot.lifecycle.timeline.LifecycleTimeline;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

/**
 * {@link SmartLifecycle} 演示。
 * <p>可控制自动启动、相位（phase）以及停机回调。优雅停机时，WebServer 会在较早 phase 停止接流，
 * 本组件 phase 设为较大值，便于在时间线里观察 stop 相对顺序。</p>
 */
@Component
public class DemoSmartLifecycle implements SmartLifecycle {

    /** 是否处于运行态 */
    private volatile boolean running;

    /**
     * 是否随上下文自动 start。
     *
     * @return true 自动启动
     */
    @Override
    public boolean isAutoStartup() {
        return true;
    }

    /**
     * 启动相位：数值越大，启动越靠后、停止越靠前（相对其它 SmartLifecycle）。
     *
     * @return 相位
     */
    @Override
    public int getPhase() {
        return 100;
    }

    /**
     * 启动逻辑。
     */
    @Override
    public void start() {
        running = true;
        LifecycleTimeline.record(
                "smart",
                "SmartLifecycle.start",
                "phase=" + getPhase() + "，模拟后台组件启动");
    }

    /**
     * 停止逻辑（带回调，Boot 优雅停机会走这个重载）。
     *
     * @param callback 停止完成回调
     */
    @Override
    public void stop(Runnable callback) {
        LifecycleTimeline.record(
                "shutdown",
                "SmartLifecycle.stop(callback)",
                "phase=" + getPhase() + "，开始停止后台组件");
        running = false;
        callback.run();
    }

    /**
     * 无回调的停止方法（兼容接口）。
     */
    @Override
    public void stop() {
        LifecycleTimeline.record("shutdown", "SmartLifecycle.stop()", "无回调停止");
        running = false;
    }

    /**
     * 是否正在运行。
     *
     * @return 运行状态
     */
    @Override
    public boolean isRunning() {
        return running;
    }
}
