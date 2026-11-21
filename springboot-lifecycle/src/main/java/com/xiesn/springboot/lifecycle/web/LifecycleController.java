package com.xiesn.springboot.lifecycle.web;

import com.xiesn.springboot.common.result.R;
import com.xiesn.springboot.lifecycle.bean.DemoLifecycleBean;
import com.xiesn.springboot.lifecycle.smart.DemoSmartLifecycle;
import com.xiesn.springboot.lifecycle.timeline.LifecycleEntry;
import com.xiesn.springboot.lifecycle.timeline.LifecycleTimeline;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 生命周期观测接口。
 */
@RestController
@RequestMapping("/api/lifecycle")
public class LifecycleController {

    private final Environment environment;
    private final DemoLifecycleBean demoLifecycleBean;
    private final DemoSmartLifecycle demoSmartLifecycle;

    /**
     * 注入环境与演示 Bean。
     *
     * @param environment         环境
     * @param demoLifecycleBean   Bean 生命周期演示
     * @param demoSmartLifecycle  SmartLifecycle 演示
     */
    public LifecycleController(Environment environment,
                               DemoLifecycleBean demoLifecycleBean,
                               DemoSmartLifecycle demoSmartLifecycle) {
        this.environment = environment;
        this.demoLifecycleBean = demoLifecycleBean;
        this.demoSmartLifecycle = demoSmartLifecycle;
    }

    /**
     * 返回完整时间线。
     *
     * @return 时间线条目
     */
    @GetMapping("/timeline")
    public R<List<LifecycleEntry>> timeline() {
        return R.ok(LifecycleTimeline.snapshot());
    }

    /**
     * 按 phase 汇总时间线。
     *
     * @return 分组结果
     */
    @GetMapping("/summary")
    public R<Map<String, List<LifecycleEntry>>> summary() {
        return R.ok(LifecycleTimeline.summaryByPhase());
    }

    /**
     * 返回当前运行态摘要，确认 Initializer / SmartLifecycle 状态。
     *
     * @return 摘要
     */
    @GetMapping("/status")
    public R<Map<String, Object>> status() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("initializerReady", environment.getProperty("lifecycle.demo.initializer-ready"));
        body.put("lifecycleBeanName", demoLifecycleBean.getBeanName());
        body.put("smartLifecycleRunning", demoSmartLifecycle.isRunning());
        body.put("serverShutdown", environment.getProperty("server.shutdown"));
        body.put("shutdownPhaseTimeout", environment.getProperty("spring.lifecycle.timeout-per-shutdown-phase"));
        body.put("timelineSize", LifecycleTimeline.snapshot().size());
        return R.ok(body);
    }
}
