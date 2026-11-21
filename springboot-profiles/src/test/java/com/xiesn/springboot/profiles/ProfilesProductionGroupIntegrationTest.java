package com.xiesn.springboot.profiles;

import com.xiesn.springboot.profiles.config.DemoAppProperties;
import com.xiesn.springboot.profiles.feature.EnvBanner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * production 组展开集成测试。
 */
@SpringBootTest
@ActiveProfiles("production")
class ProfilesProductionGroupIntegrationTest {

    @Autowired
    private Environment environment;

    @Autowired
    private DemoAppProperties demoAppProperties;

    @Autowired
    private List<EnvBanner> envBanners;

    /**
     * production group 应展开 proddb / prodmq。
     */
    @Test
    void productionGroup_shouldExpandChildProfiles() {
        Set<String> active = Arrays.stream(environment.getActiveProfiles()).collect(Collectors.toSet());
        assertTrue(active.contains("production"));
        assertTrue(active.contains("proddb"), "group 应展开 proddb，实际=" + active);
        assertTrue(active.contains("prodmq"), "group 应展开 prodmq，实际=" + active);

        assertEquals("production", demoAppProperties.getEnvName());
        // proddb / prodmq 文件后加载，覆盖 production 基线中的同名键
        assertEquals("mysql-proddb", demoAppProperties.getDatasourceLabel());
        assertEquals("hello-from-prodmq", demoAppProperties.getMessage());
        assertEquals("multidoc-production", demoAppProperties.getMultidocFlag());
        assertEquals(1, envBanners.size());
        assertEquals("production", envBanners.get(0).profile());
    }
}