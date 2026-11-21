package com.xiesn.springboot.lifecycle.web;

import com.xiesn.springboot.common.result.R;
import com.xiesn.springboot.lifecycle.timeline.LifecycleTimeline;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 长请求演示，用于配合优雅停机观察。
 * <p>先调用本接口 sleep，再停止进程：graceful 模式下应等请求结束（或等到 timeout）。</p>
 */
@RestController
@RequestMapping("/api/lifecycle")
public class SlowRequestController {

    /**
     * 模拟耗时请求。
     *
     * @param seconds 睡眠秒数，默认 5，最大 25
     * @return 完成信息
     * @throws InterruptedException 睡眠被中断
     */
    @GetMapping("/slow")
    public R<Map<String, Object>> slow(@RequestParam(defaultValue = "5") int seconds)
            throws InterruptedException {
        int safeSeconds = Math.max(1, Math.min(seconds, 25));
        LifecycleTimeline.record(
                "runtime",
                "SlowRequestController",
                "开始慢请求，sleep=" + safeSeconds + "s");
        Thread.sleep(safeSeconds * 1000L);
        LifecycleTimeline.record(
                "runtime",
                "SlowRequestController",
                "慢请求结束，sleep=" + safeSeconds + "s");
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("sleptSeconds", safeSeconds);
        body.put("tip", "可在请求未结束时停止应用，对照 server.shutdown=graceful 行为");
        return R.ok(body);
    }
}
