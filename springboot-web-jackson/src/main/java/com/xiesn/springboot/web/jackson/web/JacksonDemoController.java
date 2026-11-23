package com.xiesn.springboot.web.jackson.web;

import com.xiesn.springboot.common.result.R;
import com.xiesn.springboot.web.jackson.domain.EnterpriseOrder;
import com.xiesn.springboot.web.jackson.service.OrderJacksonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Jackson 对照接口。响应体统一走 {@code R}。
 */
@RestController
@RequestMapping("/api/jackson")
public class JacksonDemoController {

    private final OrderJacksonService orderJacksonService;

    /**
     * @param orderJacksonService 序列化对照服务
     */
    public JacksonDemoController(OrderJacksonService orderJacksonService) {
        this.orderJacksonService = orderJacksonService;
    }

    /**
     * 一次看清企业序列化、全局配置下的普通对象，以及默认 ObjectMapper 文本。
     *
     * @return 对照结果
     */
    @GetMapping("/overview")
    public R<Map<String, Object>> overview() {
        return R.ok(orderJacksonService.overview());
    }

    /**
     * 只返回企业订单，便于 api.http 单看字段。
     *
     * @return 样例订单
     */
    @GetMapping("/orders/sample")
    public R<EnterpriseOrder> sample() {
        return R.ok(orderJacksonService.sampleEnterprise());
    }

    /**
     * 按企业规则读入再写出，用来验证反序列化。
     *
     * @param order 请求体
     * @return 原样回显
     */
    @PostMapping("/orders")
    public R<EnterpriseOrder> echo(@RequestBody EnterpriseOrder order) {
        return R.ok(order);
    }
}
