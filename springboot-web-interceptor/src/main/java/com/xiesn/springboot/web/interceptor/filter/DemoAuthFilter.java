package com.xiesn.springboot.web.interceptor.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiesn.springboot.common.result.CommonErrorCode;
import com.xiesn.springboot.common.result.R;
import com.xiesn.springboot.web.interceptor.config.InterceptorDemoProperties;
import com.xiesn.springboot.web.interceptor.support.RequestChainRecorder;
import com.xiesn.springboot.web.interceptor.support.TraceConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 演示鉴权 Filter：只拦 {@code /api/interceptor/secure/**}。
 * <p>企业里完整鉴权多用 Spring Security FilterChain；这里展示「轻量 Token 网关」形态。</p>
 */
public class DemoAuthFilter extends OncePerRequestFilter {

    private final InterceptorDemoProperties properties;
    private final ObjectMapper objectMapper;

    /**
     * 注入配置与 JSON 序列化。
     *
     * @param properties   演示配置
     * @param objectMapper Jackson
     */
    public DemoAuthFilter(InterceptorDemoProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        RequestChainRecorder.record(request, "filter:auth");
        String token = request.getHeader(TraceConstants.TOKEN_HEADER);
        if (!StringUtils.hasText(token) || !token.equals(properties.getDemoToken())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(response.getWriter(),
                    R.fail(CommonErrorCode.UNAUTHORIZED, "缺少或错误的 X-Demo-Token"));
            return;
        }
        filterChain.doFilter(request, response);
    }
}
