package com.xiesn.springboot.web.restful;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * REST 规范与统一响应冒烟测试。
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WebRestfulIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static long createdId;

    /**
     * 约定总览可访问。
     */
    @Test
    @Order(1)
    void overview_shouldDescribeConventions() throws Exception {
        mockMvc.perform(get("/api/restful/overview"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.resource").value("/api/articles"));
    }

    /**
     * 创建应返回 201 + Location。
     */
    @Test
    @Order(2)
    void create_shouldReturn201WithLocation() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"UT 文章","content":"集成测试正文"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/articles/")))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.title").value("UT 文章"))
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        createdId = root.path("data").path("id").asLong();
    }

    /**
     * 列表分页与关键字过滤。
     */
    @Test
    @Order(3)
    void list_shouldSupportPageAndKeyword() throws Exception {
        mockMvc.perform(get("/api/articles")
                        .param("pageNum", "1")
                        .param("pageSize", "10")
                        .param("keyword", "UT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.total").value(1))
                .andExpect(jsonPath("$.data.records[0].id").value(createdId));
    }

    /**
     * PUT / PATCH / GET 正常路径。
     */
    @Test
    @Order(4)
    void updateAndGet_shouldWork() throws Exception {
        mockMvc.perform(put("/api/articles/{id}", createdId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"UT 全量","content":"替换后正文","status":"PUBLISHED"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("PUBLISHED"));

        mockMvc.perform(patch("/api/articles/{id}", createdId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"UT 补丁"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("UT 补丁"))
                .andExpect(jsonPath("$.data.status").value("PUBLISHED"));

        mockMvc.perform(get("/api/articles/{id}", createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(createdId));
    }

    /**
     * 两种失败风格对照。
     */
    @Test
    @Order(5)
    void styles_shouldDifferOnMissingResource() throws Exception {
        mockMvc.perform(get("/api/restful/styles/envelope/{id}", 999999))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(404));

        mockMvc.perform(get("/api/restful/styles/status/{id}", 999999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404));
    }

    /**
     * 删除成功 204；再删 404。
     */
    @Test
    @Order(6)
    void delete_shouldReturn204Then404() throws Exception {
        mockMvc.perform(delete("/api/articles/{id}", createdId))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/articles/{id}", createdId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404));
    }
}
