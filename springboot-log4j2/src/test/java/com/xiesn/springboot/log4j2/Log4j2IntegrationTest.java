package com.xiesn.springboot.log4j2;

import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 确认运行时是 Log4j2 而非 Logback，并冒烟接口。
 */
@SpringBootTest
@AutoConfigureMockMvc
class Log4j2IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * SLF4J 绑定应为 Log4j2。
     */
    @Test
    void loggerFactory_shouldBeLog4j2() {
        String name = LoggerFactory.getILoggerFactory().getClass().getName();
        assertTrue(name.toLowerCase().contains("log4j"), "实际 factory=" + name);
    }

    /**
     * status / sample / burst 接口。
     */
    @Test
    void http_shouldWork() throws Exception {
        mockMvc.perform(get("/api/log4j2/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.log4j2").value(true));

        mockMvc.perform(get("/api/log4j2/sample").param("message", "ut"))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Trace-Id"))
                .andExpect(jsonPath("$.code").value(0));

        mockMvc.perform(get("/api/log4j2/async-burst").param("lines", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.output.costMs").exists());
    }
}