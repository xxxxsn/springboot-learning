package com.xiesn.springboot.web.validation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 参数校验模块冒烟测试。
 */
@SpringBootTest
@AutoConfigureMockMvc
class WebValidationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * 总览可访问。
     */
    @Test
    void overview_shouldWork() throws Exception {
        mockMvc.perform(get("/api/validation/overview"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.port").value(8024));
    }

    /**
     * Create 成功。
     */
    @Test
    void create_shouldPass() throws Exception {
        mockMvc.perform(post("/api/validation/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "alice",
                                  "mobile": "13800138000",
                                  "status": "ENABLED",
                                  "address": {
                                    "province": "广东",
                                    "city": "深圳",
                                    "detail": "南山区"
                                  }
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.username").value("alice"))
                .andExpect(jsonPath("$.data.city").value("深圳"));
    }

    /**
     * Create 分组：手机号与枚举同时失败，应返回多字段错误。
     */
    @Test
    void create_shouldReturnMultipleFieldErrors() throws Exception {
        mockMvc.perform(post("/api/validation/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "a",
                                  "mobile": "123",
                                  "status": "UNKNOWN",
                                  "address": {
                                    "province": "广东",
                                    "city": "深圳",
                                    "detail": "南山区"
                                  }
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("参数校验失败"))
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(2))));
    }

    /**
     * 嵌套地址字段失败。
     */
    @Test
    void nested_shouldFailOnAddressCity() throws Exception {
        mockMvc.perform(post("/api/validation/users/nested-fail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "bob",
                                  "mobile": "13900139000",
                                  "status": "ENABLED",
                                  "address": {
                                    "province": "广东",
                                    "city": "",
                                    "detail": "南山区"
                                  }
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.data[0].field").value("address.city"));
    }

    /**
     * Update：路径 id 写入后再按分组校验。
     */
    @Test
    void update_shouldPassAfterPathIdBound() throws Exception {
        mockMvc.perform(put("/api/validation/users/{id}", 1001)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "alice2",
                                  "mobile": "13800138001",
                                  "status": "DISABLED",
                                  "address": {
                                    "province": "广东",
                                    "city": "广州",
                                    "detail": "天河区"
                                  }
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.id").value(1001))
                .andExpect(jsonPath("$.data.status").value("DISABLED"));
    }

    /**
     * Update：可选手机号若传入仍校验格式。
     */
    @Test
    void update_shouldValidateMobileFormatWhenPresent() throws Exception {
        mockMvc.perform(put("/api/validation/users/{id}", 1001)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "alice2",
                                  "mobile": "bad-mobile",
                                  "status": "ENABLED",
                                  "address": {
                                    "province": "广东",
                                    "city": "广州",
                                    "detail": "天河区"
                                  }
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.data[0].field").value("mobile"));
    }

    /**
     * 方法级校验失败。
     */
    @Test
    void checkMobile_shouldFailOnBadFormat() throws Exception {
        mockMvc.perform(get("/api/validation/users/check-mobile").param("mobile", "abc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.data[0].message").value("手机号格式不正确"));
    }

    /**
     * 手动 Validator 失败。
     */
    @Test
    void manual_shouldFailLikeMq() throws Exception {
        mockMvc.perform(post("/api/validation/manual")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "mq-user",
                                  "mobile": "111",
                                  "status": "ENABLED",
                                  "address": {
                                    "province": "广东",
                                    "city": "深圳",
                                    "detail": "福田"
                                  }
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("参数校验失败"))
                .andExpect(jsonPath("$.data[0].field").value("mobile"));
    }
}
