package com.xiesn.springboot.web.resolver.mvc;

import com.xiesn.springboot.common.exception.BusinessException;
import com.xiesn.springboot.common.result.CommonErrorCode;
import com.xiesn.springboot.web.resolver.annotation.CurrentUser;
import com.xiesn.springboot.web.resolver.domain.LoginUser;
import com.xiesn.springboot.web.resolver.support.LoginUserAttrs;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 解析 {@link CurrentUser}。自定义解析器由 Spring 放在 ModelAttribute 兜底之前，因此能生效。
 */
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Object attribute = request == null ? null : request.getAttribute(LoginUserAttrs.LOGIN_USER);
        if (attribute instanceof LoginUser user) {
            return user;
        }
        CurrentUser annotation = parameter.getParameterAnnotation(CurrentUser.class);
        if (annotation != null && annotation.required()) {
            throw new BusinessException(CommonErrorCode.UNAUTHORIZED, "缺少当前用户");
        }
        return null;
    }
}
