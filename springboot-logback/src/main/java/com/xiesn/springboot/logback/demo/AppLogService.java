package com.xiesn.springboot.logback.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 普通应用日志：进 app.log + app.json.log。
 */
@Service
public class AppLogService {

    private static final Logger log = LoggerFactory.getLogger(AppLogService.class);

    /**
     * 样例日志。
     *
     * @param message 消息
     * @return 结果
     */
    public Map<String, Object> sample(String message) {
        String safe = (message == null || message.isBlank()) ? "hello-logback" : message;
        log.info("[logback] sample message={} traceId={} userId={}",
                safe, MDC.get("traceId"), MDC.get("userId"));
        log.debug("[logback] sample debug (需包级别 DEBUG) message={}", safe);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("case", "MDC Pattern：traceId/userId 打进文本日志");
        body.put("input", safe);
        body.put("output", Map.of(
                "traceId", String.valueOf(MDC.get("traceId")),
                "userId", String.valueOf(MDC.get("userId")),
                "files", "logs/springboot-logback/app.log + app.json.log"));
        body.put("pitfall", "Filter finally 必须 remove MDC；JSON 文件用 StructuredLogEncoder(logstash)，MDC 会进 JSON 字段");
        return body;
    }
}