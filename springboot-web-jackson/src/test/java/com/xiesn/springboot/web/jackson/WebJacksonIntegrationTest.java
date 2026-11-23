package com.xiesn.springboot.web.jackson;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 日期、Long、枚举、空值的序列化与反序列化。
 */
@SpringBootTest
@AutoConfigureMockMvc
class WebJacksonIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * 总览同时给出企业字段、普通对象和默认 ObjectMapper 文本。
     */
    @Test
    void overview_shouldContrastEnterprisePlainAndRaw() throws Exception {
        mockMvc.perform(get("/api/jackson/overview"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.port").value(8026))
                .andExpect(jsonPath("$.data.sampleIdAfterJsNumber").value(9007199254740992L))
                .andExpect(jsonPath("$.data.enterprise.id").value("9007199254740993"))
                .andExpect(jsonPath("$.data.enterprise.status").value(20))
                .andExpect(jsonPath("$.data.enterprise.createdAt").value("2026-11-23 08:30:00"))
                .andExpect(jsonPath("$.data.enterprise.paidAt").value("2026/11/23 09:05"))
                .andExpect(jsonPath("$.data.enterprise.legacyTime").value("2026-11-23 08:30:00"))
                .andExpect(jsonPath("$.data.enterprise.amountCent").value(1990))
                .andExpect(jsonPath("$.data.enterprise.remark").doesNotExist())
                .andExpect(jsonPath("$.data.enterprise.channel").value(org.hamcrest.Matchers.nullValue()))
                .andExpect(jsonPath("$.data.plain.status").value("PAID"))
                .andExpect(jsonPath("$.data.plain.createdAt").value("2026-11-23 08:30:00"))
                .andExpect(jsonPath("$.data.plain.remark").doesNotExist())
                .andExpect(content().string(containsString("\"id\":9007199254740993")))
                .andExpect(jsonPath("$.data.rawPlainJson", containsString("2026-11-23T08:30:00")))
                .andExpect(jsonPath("$.data.rawPlainJson", containsString("\"remark\":null")));
    }

    /**
     * 样例接口只返回企业订单。
     */
    @Test
    void sample_shouldUseEnterpriseShape() throws Exception {
        mockMvc.perform(get("/api/jackson/orders/sample"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value("9007199254740993"))
                .andExpect(jsonPath("$.data.status").value(20))
                .andExpect(jsonPath("$.data.amountCent").value(1990))
                .andExpect(content().string(not(containsString("\"remark\""))));
    }

    /**
     * 字符串主键、数字码、两种日期格式可以读入再写出。
     */
    @Test
    void echo_shouldAcceptEnterpriseJson() throws Exception {
        mockMvc.perform(post("/api/jackson/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": "9007199254740993",
                                  "createdAt": "2026-11-23 08:30:00",
                                  "paidAt": "2026/11/23 09:05",
                                  "status": 20,
                                  "amountCent": 1990
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value("9007199254740993"))
                .andExpect(jsonPath("$.data.createdAt").value("2026-11-23 08:30:00"))
                .andExpect(jsonPath("$.data.paidAt").value("2026/11/23 09:05"))
                .andExpect(jsonPath("$.data.status").value(20))
                .andExpect(jsonPath("$.data.amountCent").value(1990));
    }

    /**
     * 枚举名和 JSON 数字主键也能读。
     */
    @Test
    void echo_shouldAcceptEnumNameAndNumericId() throws Exception {
        mockMvc.perform(post("/api/jackson/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": 9007199254740993,
                                  "createdAt": "2026-11-23 08:30:00",
                                  "status": "PAID",
                                  "amountCent": 1
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value("9007199254740993"))
                .andExpect(jsonPath("$.data.status").value(20));
    }

    /**
     * 全局格式不接受 ISO-8601 的 LocalDateTime。
     */
    @Test
    void echo_shouldRejectIsoLocalDateTime() throws Exception {
        mockMvc.perform(post("/api/jackson/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": "1",
                                  "createdAt": "2026-11-23T08:30:00",
                                  "status": 10,
                                  "amountCent": 1
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400));
    }
}
