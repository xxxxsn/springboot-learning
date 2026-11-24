package com.xiesn.springboot.web.cors.web;

import com.xiesn.springboot.common.result.R;
import com.xiesn.springboot.web.cors.support.CorsOrigins;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 受全局 CORS 保护的接口。{@code /shop} 再追加一个源站。
 */
@RestController
@RequestMapping("/api/cors")
public class CorsDemoController {

    /**
     * 说明白名单，并写出可被前端读取的响应头。
     *
     * @param response 当前响应
     * @return 规则
     */
    @GetMapping("/overview")
    public R<Map<String, Object>> overview(HttpServletResponse response) {
        response.setHeader(CorsOrigins.EXPOSED_HEADER, "demo-8029");
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("port", 8029);
        data.put("globalOrigin", CorsOrigins.ADMIN);
        data.put("shopOrigin", CorsOrigins.SHOP);
        data.put("rules", List.of(
                "浏览器发 Origin 才是跨域请求；同源请求没有这套响应头",
                "全局只放行 " + CorsOrigins.ADMIN + "，且允许凭证",
                "@CrossOrigin 与全局配置取并集，不会盖掉管理端",
                "/api/plain/** 没有 CORS 配置，预检会落到普通 OPTIONS"
        ));
        return R.ok(data);
    }

    /**
     * 供预检演示的写接口。
     *
     * @param body 可选正文
     * @return 原文
     */
    @PostMapping("/messages")
    public R<Map<String, String>> message(@RequestBody(required = false) Map<String, String> body) {
        return R.ok(body == null ? Map.of() : body);
    }

    /**
     * 方法级再允许商城源。Spring 会和全局源站合并。
     *
     * @return 渠道标记
     */
    @CrossOrigin(origins = CorsOrigins.SHOP)
    @GetMapping("/shop")
    public R<Map<String, String>> shop() {
        return R.ok(Map.of("channel", "shop"));
    }
}
