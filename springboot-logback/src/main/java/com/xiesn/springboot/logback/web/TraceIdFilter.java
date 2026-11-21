package com.xiesn.springboot.logback.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * 请求级 MDC：traceId + 可选 userId。
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TraceIdFilter extends OncePerRequestFilter {

    public static final String TRACE_ID = "traceId";
    public static final String USER_ID = "userId";
    public static final String HEADER_TRACE = "X-Trace-Id";
    public static final String HEADER_USER = "X-User-Id";

    /**
     * 写入并清理 MDC。
     *
     * @param request     请求
     * @param response    响应
     * @param filterChain 链
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String traceId = request.getHeader(HEADER_TRACE);
        if (traceId == null || traceId.isBlank()) {
            traceId = UUID.randomUUID().toString().replace("-", "");
        }
        String userId = request.getHeader(HEADER_USER);
        if (userId == null || userId.isBlank()) {
            userId = "anonymous";
        }
        MDC.put(TRACE_ID, traceId);
        MDC.put(USER_ID, userId);
        response.setHeader(HEADER_TRACE, traceId);
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(TRACE_ID);
            MDC.remove(USER_ID);
        }
    }
}