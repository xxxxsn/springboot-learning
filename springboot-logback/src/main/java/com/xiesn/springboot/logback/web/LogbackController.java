package com.xiesn.springboot.logback.web;

import com.xiesn.springboot.common.result.R;
import com.xiesn.springboot.logback.demo.AppLogService;
import com.xiesn.springboot.logback.demo.business.BusinessLogService;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Logback 观测接口。
 */
@RestController
@RequestMapping("/api/logback")
public class LogbackController {

    private final AppLogService appLogService;
    private final BusinessLogService businessLogService;
    private final Environment environment;

    /**
     * 注入服务与环境。
     *
     * @param appLogService       应用日志
     * @param businessLogService  业务日志
     * @param environment         环境
     */
    public LogbackController(AppLogService appLogService,
                             BusinessLogService businessLogService,
                             Environment environment) {
        this.appLogService = appLogService;
        this.businessLogService = businessLogService;
        this.environment = environment;
    }

    /**
     * 运行时摘要。
     *
     * @return 摘要
     */
    @GetMapping("/status")
    public R<Map<String, Object>> status() {
        String factory = LoggerFactory.getILoggerFactory().getClass().getName();
        Path dir = Path.of("logs", "springboot-logback");
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("loggerFactory", factory);
        body.put("logback", factory.toLowerCase().contains("logback"));
        body.put("activeProfiles", Arrays.asList(environment.getActiveProfiles()));
        body.put("jsonConsoleProfile", environment.matchesProfiles("json"));
        body.put("logDir", dir.toAbsolutePath().toString());
        body.put("logDirExists", Files.isDirectory(dir));
        body.put("hint", "默认 Pattern 控制台；--spring.profiles.active=json 切 JSON 控制台；异步对比见 #19");
        body.put("observe", Map.of(
                "mode", "日志优先",
                "trigger", "GET /api/logback/sample 与 /business",
                "files", "logs/springboot-logback/app.log、business.log、app.json.log",
                "logs", "控制台/文件搜 [logback]；看 MDC traceId/userId"
        ));
        return R.ok(body);
    }

    /**
     * 普通样例日志。
     *
     * @param message 消息
     * @return 结果
     */
    @GetMapping("/sample")
    public R<Map<String, Object>> sample(@RequestParam(defaultValue = "hello-logback") String message) {
        return R.ok(appLogService.sample(message));
    }

    /**
     * 业务分文件日志。
     *
     * @param action 动作
     * @return 结果
     */
    @GetMapping("/business")
    public R<Map<String, Object>> business(@RequestParam(defaultValue = "place-order") String action) {
        return R.ok(businessLogService.logAction(action));
    }
}