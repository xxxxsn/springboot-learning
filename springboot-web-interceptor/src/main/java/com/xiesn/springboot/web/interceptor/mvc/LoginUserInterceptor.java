package com.xiesn.springboot.web.interceptor.mvc;

import com.xiesn.springboot.web.interceptor.support.RequestAttrs;
import com.xiesn.springboot.web.interceptor.support.RequestChainRecorder;
import com.xiesn.springboot.web.interceptor.support.TraceConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 解析演示用户到 request 属性，供 Controller 读取。
 * <p>企业下一步通常是 {@code @CurrentUser} 参数解析器（见 #28）。</p>
 */
@Component
public class LoginUserInterceptor implements HandlerInterceptor {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String user = request.getHeader(TraceConstants.USER_HEADER);
        if (!StringUtils.hasText(user)) {
            user = "anonymous";
        }
        request.setAttribute(RequestAttrs.LOGIN_USER, user);
        RequestChainRecorder.record(request, "interceptor:loginUser");
        return true;
    }
}
