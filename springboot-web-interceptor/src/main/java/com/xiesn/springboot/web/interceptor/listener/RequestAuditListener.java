package com.xiesn.springboot.web.interceptor.listener;

import com.xiesn.springboot.web.interceptor.support.RequestChainRecorder;
import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 请求生命周期 Listener：init / destroy。
 * <p>适合做请求级审计计数、清理 ThreadLocal（本模块用 request 属性演示）。
 * 通过 {@code ServletListenerRegistrationBean} 注册；MockMvc 下可能不触发，需用真实容器验。</p>
 */
@Component
public class RequestAuditListener implements ServletRequestListener {

    private static final Logger log = LoggerFactory.getLogger(RequestAuditListener.class);

    /**
     * 请求进入容器。
     *
     * @param sre 事件
     */
    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        RequestChainRecorder.record(sre.getServletRequest(), "listener:request-init");
        log.debug("requestInitialized");
    }

    /**
     * 请求结束。
     *
     * @param sre 事件
     */
    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        RequestChainRecorder.record(sre.getServletRequest(), "listener:request-destroy");
        log.debug("requestDestroyed");
    }
}
