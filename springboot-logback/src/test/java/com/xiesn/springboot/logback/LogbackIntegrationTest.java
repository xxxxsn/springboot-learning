package com.xiesn.springboot.logback;

import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Logback MDC / 分文件冒烟测试。
 */
@SpringBootTest
@AutoConfigureMockMvc
class LogbackIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * 默认应是 Logback。
     */
    @Test
    void loggerFactory_shouldBeLogback() {
        String name = LoggerFactory.getILoggerFactory().getClass().getName();
        assertTrue(name.toLowerCase().contains("logback"), "实际=" + name);
    }

    /**
     * 接口与分文件。
     */
    @Test
    void http_shouldWriteSplitFiles() throws Exception {
        mockMvc.perform(get("/api/logback/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.logback").value(true));

        mockMvc.perform(get("/api/logback/sample")
                        .header("X-User-Id", "u100")
                        .param("message", "ut"))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Trace-Id"))
                .andExpect(jsonPath("$.data.output.userId").value("u100"));

        mockMvc.perform(get("/api/logback/business").param("action", "pay"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));

        assertTrue(Files.exists(Path.of("logs/springboot-logback/app.log"))
                        || Files.exists(Path.of("logs/springboot-logback/app.json.log")),
                "应生成日志文件");
        assertTrue(Files.exists(Path.of("logs/springboot-logback/business.log")),
                "业务分文件应存在");
    }
}