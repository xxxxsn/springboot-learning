package com.xiesn.springboot.web.interceptor.support;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 把 Filter / Listener / Interceptor / Controller 的触点记到 request 属性，便于对照执行顺序。
 */
public final class RequestChainRecorder {

    /** request attribute key */
    public static final String ATTR = "demo.request.chain";

    private RequestChainRecorder() {
    }

    /**
     * 追加一条链路事件。
     *
     * @param request 请求
     * @param stage   阶段名，如 filter:trace
     */
    public static void record(ServletRequest request, String stage) {
        List<String> chain = chainOf(request);
        chain.add(stage);
    }

    /**
     * 读取当前链路（只读视图）。
     *
     * @param request 请求
     * @return 事件列表
     */
    public static List<String> snapshot(HttpServletRequest request) {
        return List.copyOf(chainOf(request));
    }

    @SuppressWarnings("unchecked")
    private static List<String> chainOf(ServletRequest request) {
        Object raw = request.getAttribute(ATTR);
        if (raw instanceof List<?> list) {
            return (List<String>) list;
        }
        List<String> created = Collections.synchronizedList(new ArrayList<>());
        request.setAttribute(ATTR, created);
        return created;
    }
}
