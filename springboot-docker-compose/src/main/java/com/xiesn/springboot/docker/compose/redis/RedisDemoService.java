package com.xiesn.springboot.docker.compose.redis;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * 通过 Service Connection 注入的 Redis 做读写验证。
 */
@Service
public class RedisDemoService {

    private static final String DEMO_KEY_PREFIX = "docker-compose:demo:";

    private final StringRedisTemplate stringRedisTemplate;
    private final RedisConnectionFactory redisConnectionFactory;

    /**
     * 注入 Redis 模板与连接工厂。
     *
     * @param stringRedisTemplate    字符串模板
     * @param redisConnectionFactory 连接工厂
     */
    public RedisDemoService(StringRedisTemplate stringRedisTemplate,
                            RedisConnectionFactory redisConnectionFactory) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisConnectionFactory = redisConnectionFactory;
    }

    /**
     * Ping Redis。
     *
     * @return ping 结果
     */
    public Map<String, Object> ping() {
        String pong = Objects.requireNonNull(
                redisConnectionFactory.getConnection().ping(),
                "Redis ping 返回 null");
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("case", "Redis PING（连接来自 Docker Compose Service Connection）");
        result.put("output", pong);
        result.put("pitfall", "手写 spring.data.redis.host=localhost:6379 在临时端口映射下会连错；交给 Service Connection");
        return result;
    }

    /**
     * 写入并读回一条演示数据。
     *
     * @param value 可选值
     * @return 读写结果
     */
    public Map<String, Object> roundTrip(String value) {
        String key = DEMO_KEY_PREFIX + UUID.randomUUID();
        String safeValue = (value == null || value.isBlank()) ? "hello-docker-compose" : value;
        stringRedisTemplate.opsForValue().set(key, safeValue, Duration.ofMinutes(5));
        String read = stringRedisTemplate.opsForValue().get(key);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("case", "Redis SET/GET round-trip");
        result.put("input", Map.of("key", key, "value", safeValue));
        result.put("output", Map.of("read", read, "matched", safeValue.equals(read)));
        result.put("pitfall", "本模块依赖本机 Docker；CI 无 Docker 时应跳过或改用 Testcontainers（#21）");
        return result;
    }
}
