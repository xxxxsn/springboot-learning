package com.xiesn.springboot.web.resolver.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiesn.springboot.common.result.R;
import com.xiesn.springboot.web.resolver.annotation.ApiResult;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.nio.charset.StandardCharsets;

/**
 * 把 {@link ApiResult} 方法的返回值写成统一 {@code R}。
 * <p>必须排在 {@code RequestResponseBodyMethodProcessor} 前面，否则 {@code @RestController} 会先把原对象写出。</p>
 */
public class ApiResultReturnValueHandler implements HandlerMethodReturnValueHandler {

    private final ObjectMapper objectMapper;

    /**
     * @param objectMapper Boot 配置好的 ObjectMapper
     */
    public ApiResultReturnValueHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType.hasMethodAnnotation(ApiResult.class)
                || AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ApiResult.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest) throws Exception {
        Object body = returnValue instanceof R<?> ? returnValue : R.ok(returnValue);
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        if (response == null) {
            throw new IllegalStateException("缺少 HttpServletResponse");
        }
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), body);
        mavContainer.setRequestHandled(true);
    }
}
