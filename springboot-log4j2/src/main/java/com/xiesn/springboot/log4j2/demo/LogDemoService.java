package com.xiesn.springboot.log4j2.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 同步 vs 异步日志吞吐粗测。
 * <p>异步 Logger 名落在 {@code com.xiesn.springboot.log4j2.demo}，对应 log4j2-spring.xml 的 AsyncLogger。</p>
 */
@Service
public class LogDemoService {

    /** 走 AsyncLogger（异步写文件）。 */
    private static final Logger ASYNC_LOG = LoggerFactory.getLogger(LogDemoService.class);

    /** 走 Root 同步 RollingFile。 */
    private static final Logger SYNC_LOG = LoggerFactory.getLogger("com.xiesn.springboot.log4j2.sync.SyncProbe");

    /**
     * 打一批异步日志并计时。
     *
     * @param lines 行数
     * @return 结果
     */
    public Map<String, Object> asyncBurst(int lines) {
        int n = normalize(lines);
        long start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            ASYNC_LOG.info("async-burst i={} traceId={}", i, MDC.get("traceId"));
        }
        long costMs = (System.nanoTime() - start) / 1_000_000L;
        return result("AsyncLogger + Async Appender", n, costMs,
                "业务线程只把事件丢进 Disruptor/队列；磁盘 IO 在后台线程，调用返回更快（不等落盘）");
    }

    /**
     * 打一批同步日志并计时。
     *
     * @param lines 行数
     * @return 结果
     */
    public Map<String, Object> syncBurst(int lines) {
        int n = normalize(lines);
        long start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            SYNC_LOG.info("sync-burst i={} traceId={}", i, MDC.get("traceId"));
        }
        long costMs = (System.nanoTime() - start) / 1_000_000L;
        return result("Root 同步 RollingFile", n, costMs,
                "每条日志在业务线程上完成格式化与写 Appender，高并发时更容易拖慢接口");
    }

    /**
     * 打印一条带 MDC 的样例日志。
     *
     * @param message 消息
     * @return 结果
     */
    public Map<String, Object> sample(String message) {
        String safe = (message == null || message.isBlank()) ? "hello-log4j2" : message;
        ASYNC_LOG.info("sample message={}", safe);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("case", "MDC + PatternLayout %X{traceId}");
        body.put("input", safe);
        body.put("output", Map.of("traceId", MDC.get("traceId"), "logger", ASYNC_LOG.getName()));
        body.put("pitfall", "Filter 里 put 的 MDC 必须在 finally remove，否则线程池复用会串 traceId");
        return body;
    }

    private int normalize(int lines) {
        if (lines < 1) {
            return 100;
        }
        return Math.min(lines, 20_000);
    }

    private Map<String, Object> result(String mode, int lines, long costMs, String pitfall) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("case", mode);
        body.put("input", Map.of("lines", lines));
        body.put("output", Map.of(
                "costMs", costMs,
                "approxLinesPerSec", costMs == 0 ? lines : (lines * 1000L / costMs)));
        body.put("pitfall", pitfall);
        return body;
    }
}