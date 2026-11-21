package com.xiesn.springboot.profiles;

import com.xiesn.springboot.profiles.config.DemoAppProperties;
import com.xiesn.springboot.profiles.feature.EnvBanner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * dev profile 集成测试。
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class ProfilesDevIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DemoAppProperties demoAppProperties;

    @Autowired
    private List<EnvBanner> envBanners;

    @Autowired
    private Environment environment;

    /**
     * dev 配置与 Banner 应生效。
     */
    @Test
    void devProfile_shouldBindAndLoadDevBanner() {
        assertTrue(Arrays.asList(environment.getActiveProfiles()).contains("dev"));
        assertEquals("dev", demoAppProperties.getEnvName());
        assertEquals("hello-from-dev", demoAppProperties.getMessage());
        assertTrue(demoAppProperties.getFeature().isDebug());
        assertEquals("multidoc-dev", demoAppProperties.getMultidocFlag());
        assertEquals(1, envBanners.size());
        assertEquals("dev", envBanners.get(0).profile());
    }

    /**
     * HTTP 冒烟。
     */
    @Test
    void overview_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/profiles/overview"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.effective.envName").value("dev"));
    }
}