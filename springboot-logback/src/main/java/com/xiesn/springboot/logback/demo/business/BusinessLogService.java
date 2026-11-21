package com.xiesn.springboot.logback.demo.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 业务包日志：写入 business.log（分文件）+ JSON 文件。
 */
@Service
public class BusinessLogService {

    private static final Logger log = LoggerFactory.getLogger(BusinessLogService.class);

    /**
     * 记一条业务日志。
     *
     * @param action 动作
     * @return 说明
     */
    public Map<String, Object> logAction(String action) {
        String safe = (action == null || action.isBlank()) ? "place-order" : action;
        log.info("[logback][business] action={} traceId={} userId={}",
                safe, MDC.get("traceId"), MDC.get("userId"));
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("case", "按模块分文件：business logger → business.log");
        body.put("input", safe);
        body.put("output", Map.of(
                "logger", log.getName(),
                "files", "logs/springboot-logback/business.log + app.json.log"));
        body.put("pitfall", "additivity=false 避免同一条业务日志再进 app.log 重复一份");
        return body;
    }
}