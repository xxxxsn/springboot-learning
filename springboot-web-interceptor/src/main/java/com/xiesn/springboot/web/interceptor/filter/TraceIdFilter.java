package com.xiesn.springboot.web.interceptor.filter;

import com.xiesn.springboot.web.interceptor.support.RequestAttrs;
import com.xiesn.springboot.web.interceptor.support.RequestChainRecorder;
import com.xiesn.springboot.web.interceptor.support.TraceConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * 链路 Trace Filter：最早执行，写 MDC / 请求属性 / 响应头。
 * <p>企业常见：日志、限流、灰度都依赖这里种下的 traceId。</p>
 */
public class TraceIdFilter extends OncePerRequestFilter implements Ordered {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String traceId = request.getHeader(TraceConstants.TRACE_HEADER);
        if (!StringUtils.hasText(traceId)) {
            traceId = UUID.randomUUID().toString().replace("-", "");
        }
        MDC.put(TraceConstants.TRACE_MDC, traceId);
        request.setAttribute(RequestAttrs.TRACE_ID, traceId);
        response.setHeader(TraceConstants.TRACE_HEADER, traceId);
        RequestChainRecorder.record(request, "filter:trace");
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(TraceConstants.TRACE_MDC);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 10;
    }
}
