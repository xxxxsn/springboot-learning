package com.xiesn.springboot.web.interceptor.web;

import com.xiesn.springboot.common.result.R;
import com.xiesn.springboot.web.interceptor.config.InterceptorDemoProperties;
import com.xiesn.springboot.web.interceptor.listener.DemoSessionListener;
import com.xiesn.springboot.web.interceptor.support.RequestAttrs;
import com.xiesn.springboot.web.interceptor.support.RequestChainRecorder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 演示接口：观测执行链、鉴权 Filter、Session Listener。
 */
@RestController
@RequestMapping("/api/interceptor")
public class InterceptorDemoController {

    private final InterceptorDemoProperties properties;
    private final DemoSessionListener sessionListener;

    /**
     * 注入依赖。
     *
     * @param properties      配置
     * @param sessionListener Session 监听
     */
    public InterceptorDemoController(InterceptorDemoProperties properties,
                                     DemoSessionListener sessionListener) {
        this.properties = properties;
        this.sessionListener = sessionListener;
    }

    /**
     * 能力与职责对照。
     *
     * @return 说明
     */
    @GetMapping("/overview")
    public R<Map<String, Object>> overview() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("module", "springboot-web-interceptor");
        data.put("port", 8027);
        data.put("roles", List.of(
                Map.of("layer", "Filter", "when", "Servlet 容器，最早", "use", "Trace、鉴权、CORS、包装 Request"),
                Map.of("layer", "Listener", "when", "请求/Session 生命周期", "use", "审计、在线数、清理"),
                Map.of("layer", "Interceptor", "when", "进入 Spring MVC 后", "use", "登录用户、权限、耗时、操作日志")
        ));
        data.put("typicalOrder", List.of(
                "listener:request-init",
                "filter:trace",
                "filter:auth(仅 secure)",
                "interceptor:loginUser",
                "interceptor:preHandle",
                "controller",
                "interceptor:postHandle",
                "interceptor:afterCompletion",
                "listener:request-destroy"
        ));
        data.put("demoTokenHeader", "X-Demo-Token");
        data.put("demoTokenValue", properties.getDemoToken());
        data.put("boundary", Map.of(
                "currentUserResolver", "#28 springboot-web-resolver",
                "cors", "#29 springboot-web-cors",
                "springSecurity", "完整认证授权请用 Security FilterChain，本模块仅轻量演示"
        ));
        data.put("observe", Map.of(
                "mode", "混合",
                "api", "GET /api/interceptor/chain 看执行顺序",
                "logs", "控制台搜 [access] / [session]；Listener 需真实启动"
        ));
        return R.ok(data);
    }

    /**
     * 返回本请求已观测到的执行链（destroy 尚未发生）。
     *
     * @param request 请求
     * @return 链路快照
     */
    @GetMapping("/chain")
    public R<Map<String, Object>> chain(HttpServletRequest request) {
        RequestChainRecorder.record(request, "controller:chain");
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("chain", RequestChainRecorder.snapshot(request));
        data.put("traceId", request.getAttribute(RequestAttrs.TRACE_ID));
        data.put("loginUser", request.getAttribute(RequestAttrs.LOGIN_USER));
        return R.ok(data);
    }

    /**
     * 需鉴权接口：缺 Token → Filter 直接 401。
     *
     * @param request 请求
     * @return 成功信息
     */
    @GetMapping("/secure/ping")
    public R<Map<String, Object>> securePing(HttpServletRequest request) {
        RequestChainRecorder.record(request, "controller:secure");
        return R.ok(Map.of(
                "ok", true,
                "loginUser", request.getAttribute(RequestAttrs.LOGIN_USER),
                "chain", RequestChainRecorder.snapshot(request)
        ));
    }

    /**
     * 创建/复用 Session，触发 SessionListener。
     *
     * @param request 请求
     * @return Session 信息
     */
    @GetMapping("/session")
    public R<Map<String, Object>> session(HttpServletRequest request) {
        RequestChainRecorder.record(request, "controller:session");
        HttpSession session = request.getSession(true);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("sessionId", session.getId());
        data.put("activeSessions", sessionListener.getActiveSessions());
        data.put("chain", RequestChainRecorder.snapshot(request));
        return R.ok(data);
    }
}
