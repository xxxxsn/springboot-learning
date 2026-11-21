package com.xiesn.springboot.web.interceptor.mvc;

import com.xiesn.springboot.web.interceptor.support.RequestAttrs;
import com.xiesn.springboot.web.interceptor.support.RequestChainRecorder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 访问日志 / 耗时 Interceptor。
 * <p>只能拦进 Spring MVC 的请求；静态资源或未进 DispatcherServlet 的请求看不到。</p>
 */
@Component
public class AccessLogInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AccessLogInterceptor.class);

    /**
     * Controller 之前。
     *
     * @param request  请求
     * @param response 响应
     * @param handler  处理器
     * @return 是否继续
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(RequestAttrs.START_NANOS, System.nanoTime());
        RequestChainRecorder.record(request, "interceptor:preHandle");
        return true;
    }

    /**
     * Controller 返回之后、视图渲染前（REST 通常无视图）。
     *
     * @param request      请求
     * @param response     响应
     * @param handler      处理器
     * @param modelAndView 视图
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) {
        RequestChainRecorder.record(request, "interceptor:postHandle");
    }

    /**
     * 整个请求结束（含异常），适合统计耗时。
     *
     * @param request  请求
     * @param response 响应
     * @param handler  处理器
     * @param ex       异常
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        Object start = request.getAttribute(RequestAttrs.START_NANOS);
        long costMs = 0L;
        if (start instanceof Long nanos) {
            costMs = (System.nanoTime() - nanos) / 1_000_000L;
        }
        request.setAttribute(RequestAttrs.COST_MS, costMs);
        response.setHeader("X-Cost-Ms", String.valueOf(costMs));
        RequestChainRecorder.record(request, "interceptor:afterCompletion");
        log.info("[access] {} {} status={} costMs={} ex={}",
                request.getMethod(), request.getRequestURI(), response.getStatus(), costMs,
                ex == null ? "-" : ex.getClass().getSimpleName());
    }
}
