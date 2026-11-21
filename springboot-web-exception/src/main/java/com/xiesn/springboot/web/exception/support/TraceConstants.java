package com.xiesn.springboot.web.exception.support;

/**
 * 链路追踪常量。
 */
public final class TraceConstants {

    /** 请求头 / 响应头 */
    public static final String TRACE_HEADER = "X-Trace-Id";

    /** MDC key */
    public static final String TRACE_MDC = "traceId";

    private TraceConstants() {
    }
}
