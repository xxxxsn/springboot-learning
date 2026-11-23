package com.xiesn.springboot.web.resolver.filter;

import com.xiesn.springboot.web.resolver.domain.LoginUser;
import com.xiesn.springboot.web.resolver.support.LoginUserAttrs;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 只把请求头转成 {@link LoginUser}，不决定接口是否放行。
 */
@Component
public class DemoUserFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(DemoUserFilter.class);

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String idText = request.getHeader(LoginUserAttrs.USER_ID_HEADER);
        if (StringUtils.hasText(idText)) {
            bindUser(request, idText.trim());
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 编号合法时写入 request。非法编号留给解析器按 required 处理。
     *
     * @param request 当前请求
     * @param idText  请求头中的用户编号
     */
    private void bindUser(HttpServletRequest request, String idText) {
        try {
            long id = Long.parseLong(idText);
            String name = request.getHeader(LoginUserAttrs.USER_NAME_HEADER);
            if (!StringUtils.hasText(name)) {
                name = "anonymous";
            }
            request.setAttribute(LoginUserAttrs.LOGIN_USER, new LoginUser(id, name.trim()));
        } catch (NumberFormatException ex) {
            log.warn("[resolver] {} 不是数字", LoginUserAttrs.USER_ID_HEADER);
        }
    }
}
