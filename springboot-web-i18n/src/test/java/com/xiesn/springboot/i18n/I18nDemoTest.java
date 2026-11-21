package com.xiesn.springboot.i18n;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 三个业务场景返回 key；MessageSource 按请求语言出句子；两套前端文案的 key 对齐。
 */
@SpringBootTest
@AutoConfigureMockMvc
class I18nDemoTest {

    private static final Set<String> KEYS = Set.of(
            "common.param_invalid",
            "auth.login.invalid_credentials",
            "file.type_not_allowed"
    );

    @Autowired
    private MockMvc mockMvc;

    /**
     * 口令错误时 message 必须是 key，不能是中文句子。
     */
    @Test
    void loginFailure_returnsKey() throws Exception {
        mockMvc.perform(post("/api/i18n/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"ada\",\"password\":\"wrong\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(41001))
                .andExpect(jsonPath("$.message").value("auth.login.invalid_credentials"));
    }

    /**
     * 教学口令通过时不返回错误 key。
     */
    @Test
    void loginSuccess_returnsUsername() throws Exception {
        mockMvc.perform(post("/api/i18n/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"ada\",\"password\":\"secret\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data").value("ada"));
    }

    /**
     * 缺用户名走校验，统一收成 common.param_invalid。
     */
    @Test
    void blankUsername_returnsParamKey() throws Exception {
        mockMvc.perform(post("/api/i18n/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"\",\"password\":\"secret\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(40000))
                .andExpect(jsonPath("$.message").value("common.param_invalid"));
    }

    /**
     * 扩展名不在白名单时返回文件类型 key。
     */
    @Test
    void rejectedExtension_returnsFileKey() throws Exception {
        mockMvc.perform(post("/api/i18n/files")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"filename\":\"note.exe\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(44002))
                .andExpect(jsonPath("$.message").value("file.type_not_allowed"));
    }

    /**
     * 英文请求头得到英文句子，中文请求头得到中文句子。
     */
    @Test
    void messageSource_followsAcceptLanguage() throws Exception {
        mockMvc.perform(get("/api/i18n/messages")
                        .param("key", "auth.login.invalid_credentials")
                        .header("Accept-Language", "en"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.text").value("Incorrect username or password"));

        mockMvc.perform(get("/api/i18n/messages")
                        .param("key", "auth.login.invalid_credentials")
                        .header("Accept-Language", "zh-CN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.text").value("用户名或密码错误"));
    }

    /**
     * vue-i18n 与 Nuxt 的中英文文件都必须覆盖同一组 key。
     */
    @Test
    void frontendCatalogs_coverSameKeys() throws Exception {
        assertKeys("frontend-catalog/vue/zh-CN.yaml", true);
        assertKeys("frontend-catalog/vue/en.yaml", true);
        assertKeys("frontend-catalog/nuxt/zh-CN.json", false);
        assertKeys("frontend-catalog/nuxt/en.json", false);
    }

    private static void assertKeys(String path, boolean yaml) throws Exception {
        Set<String> found = new HashSet<>();
        try (InputStream input = new ClassPathResource(path).getInputStream()) {
            if (yaml) {
                Object loaded = new Yaml().load(input);
                flatten("errors", ((Map<?, ?>) loaded).get("errors"), found);
            } else {
                JsonNode errors = new ObjectMapper().readTree(input).get("errors");
                flatten("errors", errors, found);
            }
        }
        if (!found.equals(KEYS)) {
            throw new AssertionError(path + " keys=" + found);
        }
    }

    private static void flatten(String prefix, Object node, Set<String> found) {
        if (node instanceof Map<?, ?> map) {
            map.forEach((key, value) -> flatten(prefix + "." + key, value, found));
            return;
        }
        if (node instanceof JsonNode json && json.isObject()) {
            json.fieldNames().forEachRemaining(name -> flatten(prefix + "." + name, json.get(name), found));
            return;
        }
        found.add(prefix.substring("errors.".length()));
    }
}
