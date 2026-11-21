package com.xiesn.springboot.maven.multimodule.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 多模块装配冒烟测试：确认 web 能扫到 service 实现。
 */
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * 列表接口应返回成功码与非空 records。
     */
    @Test
    void listProducts_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data[0].name").exists());
    }

    /**
     * 查询不存在商品应走业务异常，返回 404 业务码。
     */
    @Test
    void missingProduct_shouldReturnBusinessError() throws Exception {
        mockMvc.perform(get("/api/products/{id}", 999))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(404));
    }
}
