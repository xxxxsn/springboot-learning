package com.xiesn.springboot.web.interceptor.listener;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Session 监听：在线会话数演示（企业常用于在线用户统计的简化版）。
 * <p>MockMvc 的 MockHttpSession 不会回调本监听器，请用随机端口集成测试验证。</p>
 */
@Component
public class DemoSessionListener implements HttpSessionListener {

    private static final Logger log = LoggerFactory.getLogger(DemoSessionListener.class);

    private final AtomicInteger activeSessions = new AtomicInteger();

    /**
     * Session 创建。
     *
     * @param se 事件
     */
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        int count = activeSessions.incrementAndGet();
        log.info("[session] created id={} active={}", se.getSession().getId(), count);
    }

    /**
     * Session 销毁。
     *
     * @param se 事件
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        int count = activeSessions.decrementAndGet();
        log.info("[session] destroyed id={} active={}", se.getSession().getId(), count);
    }

    /**
     * 当前活跃 Session 数。
     *
     * @return 数量
     */
    public int getActiveSessions() {
        return activeSessions.get();
    }
}
