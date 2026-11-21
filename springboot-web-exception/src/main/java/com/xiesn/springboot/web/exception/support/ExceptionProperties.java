package com.xiesn.springboot.web.exception.support;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 异常响应风格配置。
 */
@ConfigurationProperties(prefix = "demo.exception")
public class ExceptionProperties {

    /**
     * 响应风格。
     */
    public enum Style {
        /** 国内常见：统一错误体 + HTTP 状态对齐 */
        ENVELOPE,
        /** RFC 7807 ProblemDetail */
        PROBLEM_DETAIL
    }

    /** 默认信封模式 */
    private Style style = Style.ENVELOPE;

    /**
     * 获取响应风格。
     *
     * @return 风格
     */
    public Style getStyle() {
        return style;
    }

    /**
     * 设置响应风格。
     *
     * @param style 风格
     */
    public void setStyle(Style style) {
        this.style = style;
    }
}
