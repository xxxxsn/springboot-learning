package com.xiesn.springboot.docker.compose;

import com.xiesn.springboot.docker.compose.redis.RedisDemoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 依赖本机 Docker 的集成测试（test 资源里将 skip.in-tests=false）。
 */
@SpringBootTest
@AutoConfigureMockMvc
class DockerComposeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RedisDemoService redisDemoService;

    /**
     * Redis ping 应成功。
     */
    @Test
    void redisPing_shouldReturnPong() {
        Map<String, Object> result = redisDemoService.ping();
        assertEquals("PONG", result.get("output"));
    }

    /**
     * 读写往返应一致。
     */
    @Test
    void redisRoundTrip_shouldMatch() {
        Map<String, Object> result = redisDemoService.roundTrip("compose-test");
        @SuppressWarnings("unchecked")
        Map<String, Object> output = (Map<String, Object>) result.get("output");
        assertTrue((Boolean) output.get("matched"));
        assertEquals("compose-test", output.get("read"));
    }

    /**
     * HTTP 冒烟。
     */
    @Test
    void http_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/docker-compose/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.composeFile").value("compose.yaml"));

        mockMvc.perform(get("/api/docker-compose/redis/ping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.output").value("PONG"));
    }
}
