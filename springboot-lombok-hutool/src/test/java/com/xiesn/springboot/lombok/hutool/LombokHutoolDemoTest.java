package com.xiesn.springboot.lombok.hutool;

import com.xiesn.springboot.lombok.hutool.guava.GuavaDemoService;
import com.xiesn.springboot.lombok.hutool.hutool.HutoolDemoService;
import com.xiesn.springboot.lombok.hutool.lombok.LombokDemoService;
import com.xiesn.springboot.lombok.hutool.lombok.model.BuilderUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Lombok / Hutool / Guava 核心行为与接口冒烟测试。
 */
@SpringBootTest
@AutoConfigureMockMvc
class LombokHutoolDemoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LombokDemoService lombokDemoService;

    @Autowired
    private HutoolDemoService hutoolDemoService;

    @Autowired
    private GuavaDemoService guavaDemoService;

    /**
     * equals 坑：@Data 在集合变更后 equals 变 false；SafeEmployee 仍相等。
     */
    @Test
    void lombokEquals_shouldShowCollectionPitfall() {
        Map<String, Object> result = lombokDemoService.equalsDemo();
        @SuppressWarnings("unchecked")
        Map<String, Object> output = (Map<String, Object>) result.get("output");
        assertTrue((Boolean) output.get("dataEmployeeEqualBeforeMutate"));
        assertFalse((Boolean) output.get("dataEmployeeEqualAfterMutate"));
        assertTrue((Boolean) output.get("safeEmployeeEqualDespiteRoleDiff"));
    }

    /**
     * Hutool Bean 拷贝应复制同名字段。
     */
    @Test
    void hutoolBeanCopy_shouldCopyFields() {
        Map<String, Object> result = hutoolDemoService.demoBean();
        @SuppressWarnings("unchecked")
        Map<String, Object> output = (Map<String, Object>) result.get("output");
        assertEquals("王五", output.get("targetName"));
        assertEquals(28, output.get("targetAge"));
    }

    /**
     * Guava partition 应按 size 切段。
     */
    @Test
    void guavaPartition_shouldSplitBySize() {
        Map<String, Object> result = guavaDemoService.demoPartition();
        @SuppressWarnings("unchecked")
        List<List<Integer>> parts = (List<List<Integer>>) result.get("output");
        assertEquals(3, parts.size());
        assertEquals(List.of(1, 2, 3), parts.get(0));
        assertEquals(List.of(7), parts.get(2));
    }

    /**
     * HTTP 接口冒烟。
     */
    @Test
    void httpEndpoints_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/lombok/overview"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));

        mockMvc.perform(post("/api/lombok/jackson")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"demo\",\"age\":20}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.output.username").value("demo"));

        mockMvc.perform(get("/api/hutool/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.str").exists());

        mockMvc.perform(get("/api/guava/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.partition").exists());
    }

    /**
     * BuilderUser 能被服务层正常处理。
     */
    @Test
    void builderUser_shouldWorkWithService() {
        BuilderUser user = BuilderUser.builder().username("u").age(18).build();
        Map<String, Object> result = lombokDemoService.jackson(user);
        assertEquals("Builder + Jackson：无参/全参齐全才能反序列化", result.get("case"));
    }
}
