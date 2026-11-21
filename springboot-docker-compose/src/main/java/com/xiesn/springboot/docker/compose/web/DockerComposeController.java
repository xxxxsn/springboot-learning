package com.xiesn.springboot.docker.compose.web;

import com.xiesn.springboot.common.result.R;
import com.xiesn.springboot.docker.compose.redis.RedisDemoService;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Docker Compose 演示 HTTP 入口。
 */
@RestController
@RequestMapping("/api/docker-compose")
public class DockerComposeController {

    private final RedisDemoService redisDemoService;
    private final Environment environment;

    /**
     * 注入演示服务与环境。
     *
     * @param redisDemoService Redis 演示
     * @param environment      环境
     */
    public DockerComposeController(RedisDemoService redisDemoService, Environment environment) {
        this.redisDemoService = redisDemoService;
        this.environment = environment;
    }

    /**
     * 配置与运行态摘要。
     *
     * @return 摘要
     */
    @GetMapping("/status")
    public R<Map<String, Object>> status() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("composeFile", environment.getProperty("spring.docker.compose.file"));
        body.put("composeEnabled", environment.getProperty("spring.docker.compose.enabled"));
        body.put("lifecycleManagement", environment.getProperty("spring.docker.compose.lifecycle-management"));
        body.put("skipInTests", environment.getProperty("spring.docker.compose.skip.in-tests"));
        body.put("hint", "启动日志中应出现 Docker Compose / Service connection 相关信息；sidecar 因 ignore 标签不会建连");
        return R.ok(body);
    }

    /**
     * Redis ping。
     *
     * @return ping 结果
     */
    @GetMapping("/redis/ping")
    public R<Map<String, Object>> redisPing() {
        return R.ok(redisDemoService.ping());
    }

    /**
     * Redis 读写往返。
     *
     * @param value 写入值
     * @return 结果
     */
    @GetMapping("/redis/round-trip")
    public R<Map<String, Object>> redisRoundTrip(
            @RequestParam(defaultValue = "hello-docker-compose") String value) {
        return R.ok(redisDemoService.roundTrip(value));
    }
}
