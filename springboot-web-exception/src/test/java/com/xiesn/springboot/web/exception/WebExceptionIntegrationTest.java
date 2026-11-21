package com.xiesn.springboot.web.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 企业异常封装冒烟测试（默认 ENVELOPE）。
 */
@SpringBootTest
@AutoConfigureMockMvc
class WebExceptionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * 总览。
     */
    @Test
    void overview_shouldExposeStyle() throws Exception {
        mockMvc.perform(get("/api/exception/overview"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.style").value("ENVELOPE"))
                .andExpect(header().exists("X-Trace-Id"));
    }

    /**
     * 域错误码 404 + traceId。
     */
    @Test
    void getOrder_missing_shouldReturn404Envelope() throws Exception {
        mockMvc.perform(get("/api/exception/orders/{id}", 999999)
                        .header("X-Trace-Id", "trace-ut-001"))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Trace-Id", "trace-ut-001"))
                .andExpect(jsonPath("$.code").value(20404))
                .andExpect(jsonPath("$.message").value("订单不存在"))
                .andExpect(jsonPath("$.traceId").value("trace-ut-001"))
                .andExpect(jsonPath("$.path").value("/api/exception/orders/999999"));
    }

    /**
     * 状态冲突 409。
     */
    @Test
    void cancel_cancelledOrder_shouldConflict() throws Exception {
        mockMvc.perform(post("/api/exception/orders/{id}/cancel", 1002))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(20409));
    }

    /**
     * 校验多字段错误。
     */
    @Test
    void create_invalidBody_shouldReturnFieldErrors() throws Exception {
        mockMvc.perform(post("/api/exception/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"sku":"","quantity":0}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("参数校验失败"))
                .andExpect(jsonPath("$.errors", hasSize(greaterThanOrEqualTo(2))));
    }

    /**
     * 库存不足业务码。
     */
    @Test
    void create_stockNotEnough_shouldBadRequest() throws Exception {
        mockMvc.perform(post("/api/exception/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"sku":"SKU-1","quantity":101}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(20400));
    }

    /**
     * 无权限 403。
     */
    @Test
    void forbidden_shouldReturn403() throws Exception {
        mockMvc.perform(get("/api/exception/forbidden"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(403));
    }

    /**
     * 系统异常脱敏，不回传堆栈文案。
     */
    @Test
    void boom_shouldHideInternalMessage() throws Exception {
        mockMvc.perform(get("/api/exception/boom"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("服务器内部错误"))
                .andExpect(jsonPath("$.message", not(containsString("NPE"))));
    }

    /**
     * 缺少参数。
     */
    @Test
    void needParam_missing_shouldBadRequest() throws Exception {
        mockMvc.perform(get("/api/exception/need-param"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("id"));
    }

    /**
     * 类型不匹配。
     */
    @Test
    void getOrder_typeMismatch_shouldBadRequest() throws Exception {
        mockMvc.perform(get("/api/exception/orders/{id}", "abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400));
    }

    /**
     * 未映射路径 404。
     */
    @Test
    void unknownPath_shouldReturn404() throws Exception {
        mockMvc.perform(get("/api/exception/no-such-api"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404));
    }
}
