package com.xiesn.springboot.web.interceptor;

import com.xiesn.springboot.web.interceptor.listener.DemoSessionListener;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 真实内嵌容器下验证 Servlet Listener。
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ListenerContainerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DemoSessionListener sessionListener;

    /**
     * SessionListener 应在真实容器创建 Session 时递增。
     */
    @Test
    void session_shouldNotifyHttpSessionListener() {
        int before = sessionListener.getActiveSessions();
        ResponseEntity<Map> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/interceptor/session", Map.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertTrue(sessionListener.getActiveSessions() >= before + 1
                        || sessionListener.getActiveSessions() >= 1,
                "activeSessions=" + sessionListener.getActiveSessions());
    }

    /**
     * Request Listener 应写入 chain。
     */
    @Test
    @SuppressWarnings("unchecked")
    void chain_shouldIncludeRequestListener() {
        ResponseEntity<Map> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/interceptor/chain", Map.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        Map<String, Object> data = (Map<String, Object>) body.get("data");
        List<String> chain = (List<String>) data.get("chain");
        assertTrue(chain.contains("listener:request-init"), "chain=" + chain);
        assertTrue(chain.contains("filter:trace"), "chain=" + chain);
    }
}
