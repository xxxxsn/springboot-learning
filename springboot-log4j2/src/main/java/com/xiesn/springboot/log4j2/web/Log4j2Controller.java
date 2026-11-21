package com.xiesn.springboot.log4j2.web;

import com.xiesn.springboot.common.result.R;
import com.xiesn.springboot.log4j2.demo.LogDemoService;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Log4j2 观测接口。
 */
@RestController
@RequestMapping("/api/log4j2")
public class Log4j2Controller {

    private final LogDemoService logDemoService;

    /**
     * 注入演示服务。
     *
     * @param logDemoService 服务
     */
    public Log4j2Controller(LogDemoService logDemoService) {
        this.logDemoService = logDemoService;
    }

    /**
     * 运行时日志实现摘要。
     *
     * @return 摘要
     */
    @GetMapping("/status")
    public R<Map<String, Object>> status() {
        Map<String, Object> body = new LinkedHashMap<>();
        Object factory = LoggerFactory.getILoggerFactory();
        body.put("loggerFactory", factory.getClass().getName());
        body.put("expected", "org.apache.logging.slf4j.Log4jLoggerFactory");
        body.put("log4j2", factory.getClass().getName().toLowerCase().contains("log4j"));
        body.put("hint", "若仍是 Logback LoggerContext，说明 spring-boot-starter-logging 未排除干净");
        body.put("configFile", "classpath:log4j2-spring.xml");
        body.put("logDir", "logs/springboot-log4j2/");
        return R.ok(body);
    }

    /**
     * 样例日志（带 MDC）。
     *
     * @param message 消息
     * @return 结果
     */
    @GetMapping("/sample")
    public R<Map<String, Object>> sample(@RequestParam(defaultValue = "hello-log4j2") String message) {
        return R.ok(logDemoService.sample(message));
    }

    /**
     * 异步突发写日志。
     *
     * @param lines 行数
     * @return 耗时
     */
    @GetMapping("/async-burst")
    public R<Map<String, Object>> asyncBurst(@RequestParam(defaultValue = "2000") int lines) {
        return R.ok(logDemoService.asyncBurst(lines));
    }

    /**
     * 同步突发写日志。
     *
     * @param lines 行数
     * @return 耗时
     */
    @GetMapping("/sync-burst")
    public R<Map<String, Object>> syncBurst(@RequestParam(defaultValue = "2000") int lines) {
        return R.ok(logDemoService.syncBurst(lines));
    }
}