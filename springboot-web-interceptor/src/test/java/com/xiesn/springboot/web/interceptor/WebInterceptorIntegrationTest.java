package com.xiesn.springboot.web.interceptor;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Filter / Interceptor 冒烟（MockMvc）。
 * <p>Servlet Listener 在 MockMvc 下通常不触发，见 {@link ListenerContainerTest}。</p>
 */
@SpringBootTest
@AutoConfigureMockMvc
class WebInterceptorIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * 总览可访问。
     */
    @Test
    void overview_shouldWork() throws Exception {
        mockMvc.perform(get("/api/interceptor/overview"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.port").value(8027))
                .andExpect(header().exists("X-Trace-Id"))
                .andExpect(header().exists("X-Cost-Ms"));
    }

    /**
     * 执行链：Filter → Interceptor → Controller。
     */
    @Test
    void chain_shouldShowOrderedStages() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/interceptor/chain")
                        .header("X-Trace-Id", "chain-ut")
                        .header("X-Demo-User", "alice"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Trace-Id", "chain-ut"))
                .andExpect(jsonPath("$.data.loginUser").value("alice"))
                .andExpect(jsonPath("$.data.traceId").value("chain-ut"))
                .andReturn();

        String body = result.getResponse().getContentAsString();
        List<String> chain = JsonPath.read(body, "$.data.chain");
        assertTrue(chain.contains("filter:trace"), "chain=" + chain);
        assertTrue(chain.contains("interceptor:loginUser"), "chain=" + chain);
        assertTrue(chain.contains("interceptor:preHandle"), "chain=" + chain);
        assertTrue(chain.contains("controller:chain"), "chain=" + chain);

        int filterIdx = chain.indexOf("filter:trace");
        int preIdx = chain.indexOf("interceptor:preHandle");
        int ctrlIdx = chain.indexOf("controller:chain");
        assertTrue(filterIdx < preIdx && preIdx < ctrlIdx, "顺序异常: " + chain);
    }

    /**
     * 无 Token 被鉴权 Filter 拦截。
     */
    @Test
    void secure_withoutToken_shouldUnauthorized() throws Exception {
        mockMvc.perform(get("/api/interceptor/secure/ping"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401));
    }

    /**
     * 正确 Token 放行。
     */
    @Test
    void secure_withToken_shouldOk() throws Exception {
        mockMvc.perform(get("/api/interceptor/secure/ping")
                        .header("X-Demo-Token", "demo-token-8027")
                        .header("X-Demo-User", "bob"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.ok").value(true))
                .andExpect(jsonPath("$.data.loginUser").value("bob"))
                .andExpect(jsonPath("$.data.chain").isArray());
    }
}
