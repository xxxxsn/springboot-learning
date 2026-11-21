package com.xiesn.springboot.lifecycle;

import com.xiesn.springboot.lifecycle.timeline.LifecycleEntry;
import com.xiesn.springboot.lifecycle.timeline.LifecycleTimeline;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 启动后时间线应包含关键阶段。
 */
@SpringBootTest
@AutoConfigureMockMvc
class LifecycleTimelineIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * 校验时间线包含 bootstrap / bean / runner / event / smart。
     */
    @Test
    void timeline_shouldContainKeyPhases() throws Exception {
        List<LifecycleEntry> entries = LifecycleTimeline.snapshot();
        Set<String> phases = entries.stream().map(LifecycleEntry::getPhase).collect(Collectors.toSet());
        assertTrue(phases.contains("bootstrap"), "应包含 bootstrap");
        assertTrue(phases.contains("bean"), "应包含 bean");
        assertTrue(phases.contains("runner"), "应包含 runner");
        assertTrue(phases.contains("event"), "应包含 event");
        assertTrue(phases.contains("smart"), "应包含 smart");

        mockMvc.perform(get("/api/lifecycle/timeline"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data[0].sequence").exists());
    }

    /**
     * 校验 Initializer 写入的属性可读。
     */
    @Test
    void status_shouldShowInitializerFlag() throws Exception {
        mockMvc.perform(get("/api/lifecycle/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.initializerReady").value("true"))
                .andExpect(jsonPath("$.data.smartLifecycleRunning").value(true));
    }
}
