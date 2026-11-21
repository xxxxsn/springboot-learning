package com.xiesn.springboot.spi.plugin;

import com.xiesn.springboot.spi.plugin.service.PluginDemoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * SPI / 插件发现集成测试。
 */
@SpringBootTest
@AutoConfigureMockMvc
class PluginDemoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PluginDemoService pluginDemoService;

    /**
     * overview 应包含三类发现结果。
     */
    @Test
    void overview_shouldContainThreeDiscoveryChannels() {
        Map<String, Object> overview = pluginDemoService.overview();
        @SuppressWarnings("unchecked")
        List<Map<String, String>> javaSpi = (List<Map<String, String>>) overview.get("javaSpi");
        @SuppressWarnings("unchecked")
        List<Map<String, String>> springBean = (List<Map<String, String>>) overview.get("springBean");
        @SuppressWarnings("unchecked")
        List<Map<String, String>> springFactories = (List<Map<String, String>>) overview.get("springFactories");

        assertEquals(2, javaSpi.size());
        assertEquals(2, springBean.size());
        assertEquals(2, springFactories.size());
        assertTrue(javaSpi.stream().anyMatch(i -> "sms".equals(i.get("name"))));
        assertTrue(springBean.stream().anyMatch(i -> "push".equals(i.get("name"))));
        assertTrue(springFactories.stream().anyMatch(i -> "audit".equals(i.get("name"))));
    }

    /**
     * 调用 Java SPI / Spring Bean 插件。
     */
    @Test
    void invoke_shouldWorkForBothChannels() {
        Map<String, Object> sms = pluginDemoService.invokeJavaSpi("sms", "hi");
        assertEquals("[SMS] 已发送: hi", sms.get("output"));

        Map<String, Object> push = pluginDemoService.invokeSpringBean("push", "hi");
        assertEquals("[PUSH] 已推送: hi", push.get("output"));
    }

    /**
     * HTTP 冒烟。
     */
    @Test
    void http_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/spi/overview"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.javaSpi").isArray());

        mockMvc.perform(get("/api/spi/java/email").param("payload", "mail"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.output").value("[EMAIL] 已发送: mail"));

        mockMvc.perform(get("/api/spi/spring/wechat").param("payload", "wx"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.output").value("[WECHAT] 已推送: wx"));
    }
}
