package com.xiesn.springboot.web.interceptor.support;

/**
 * 请求属性常量（企业里常放 LoginUser / 耗时起点等）。
 */
public final class RequestAttrs {

    /** 链路 traceId */
    public static final String TRACE_ID = "demo.traceId";

    /** 演示登录用户 */
    public static final String LOGIN_USER = "demo.loginUser";

    /** Interceptor 计时起点（nanos） */
    public static final String START_NANOS = "demo.startNanos";

    /** Interceptor 耗时毫秒 */
    public static final String COST_MS = "demo.costMs";

    private RequestAttrs() {
    }
}
