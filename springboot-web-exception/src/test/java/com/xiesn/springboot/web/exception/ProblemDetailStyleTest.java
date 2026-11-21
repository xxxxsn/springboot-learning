package com.xiesn.springboot.web.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * ProblemDetail 风格冒烟。
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("problem")
class ProblemDetailStyleTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * 业务 404 应返回 RFC7807 结构 + 扩展 code/traceId。
     */
    @Test
    void missingOrder_shouldReturnProblemDetail() throws Exception {
        mockMvc.perform(get("/api/exception/orders/{id}", 999999)
                        .header("X-Trace-Id", "pd-trace-1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("订单不存在"))
                .andExpect(jsonPath("$.code").value(20404))
                .andExpect(jsonPath("$.traceId").value("pd-trace-1"));
    }
}
