package com.xiesn.springboot.web.interceptor.support;

/**
 * Trace 相关常量。
 */
public final class TraceConstants {

    /** 请求/响应头 */
    public static final String TRACE_HEADER = "X-Trace-Id";

    /** MDC key */
    public static final String TRACE_MDC = "traceId";

    /** 演示鉴权头 */
    public static final String TOKEN_HEADER = "X-Demo-Token";

    /** 演示用户头（Interceptor 解析） */
    public static final String USER_HEADER = "X-Demo-User";

    private TraceConstants() {
    }
}
