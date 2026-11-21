package com.xiesn.springboot.web.exception.web;

import com.xiesn.springboot.common.result.R;
import com.xiesn.springboot.web.exception.dto.OrderCreateRequest;
import com.xiesn.springboot.web.exception.service.OrderExceptionDemoService;
import com.xiesn.springboot.web.exception.support.ExceptionProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 异常场景演示接口。
 */
@RestController
@RequestMapping("/api/exception")
@Validated
public class ExceptionDemoController {

    private final OrderExceptionDemoService orderService;
    private final ExceptionProperties exceptionProperties;

    /**
     * 注入服务与配置。
     *
     * @param orderService         订单服务
     * @param exceptionProperties  异常风格
     */
    public ExceptionDemoController(OrderExceptionDemoService orderService,
                                   ExceptionProperties exceptionProperties) {
        this.orderService = orderService;
        this.exceptionProperties = exceptionProperties;
    }

    /**
     * 能力总览。
     *
     * @return 说明
     */
    @GetMapping("/overview")
    public R<Map<String, Object>> overview() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("module", "springboot-web-exception");
        data.put("port", 8025);
        data.put("style", exceptionProperties.getStyle().name());
        data.put("features", List.of(
                "ErrorCode → HTTP 状态映射",
                "域错误码 OrderErrorCode（HttpStatusErrorCode）",
                "traceId（MDC + 响应头 + 错误体）",
                "校验错误列表 errors[]",
                "ENVELOPE / PROBLEM_DETAIL 双模式",
                "系统异常脱敏兜底"
        ));
        data.put("sinkTip", "实战将 support/* 与域 ErrorCode 约定下沉到公司 common/framework");
        data.put("boundary", Map.of(
                "restful", "#23",
                "validation", "#24",
                "i18n", "#31"
        ));
        data.put("observe", Map.of(
                "mode", "混合",
                "api", "看 HTTP 状态 + code/traceId；/boom 响应应脱敏",
                "logs", "控制台搜 [exception]；/boom 堆栈只在日志"
        ));
        return R.ok(data);
    }

    /**
     * 查询订单：存在 200；不存在 404 + 域错误码。
     *
     * @param id 订单 id
     * @return 成功信封
     */
    @GetMapping("/orders/{id}")
    public R<Map<String, Object>> getOrder(@PathVariable Long id) {
        return R.ok(orderService.getOrder(id));
    }

    /**
     * 取消订单：状态冲突 → 409。
     *
     * @param id 订单 id
     * @return 成功信封
     */
    @PostMapping("/orders/{id}/cancel")
    public R<Map<String, Object>> cancel(@PathVariable Long id) {
        return R.ok(orderService.cancel(id));
    }

    /**
     * 创建订单：校验失败 / 库存不足。
     *
     * @param request 入参
     * @return 成功信封
     */
    @PostMapping("/orders")
    public R<Map<String, Object>> create(@Valid @RequestBody OrderCreateRequest request) {
        return R.ok(orderService.create(request));
    }

    /**
     * 方法级参数校验。
     *
     * @param amount 金额
     * @return 成功
     */
    @GetMapping("/assert-amount")
    public R<Map<String, Object>> assertAmount(@RequestParam @Min(1) Integer amount) {
        return R.ok(Map.of("amount", amount));
    }

    /**
     * 缺少必填 query（不传 id）。
     *
     * @param id 必填参数
     * @return 成功
     */
    @GetMapping("/need-param")
    public R<Map<String, Object>> needParam(@RequestParam Long id) {
        return R.ok(Map.of("id", id));
    }

    /**
     * 无权限。
     *
     * @return 不会成功返回
     */
    @GetMapping("/forbidden")
    public R<Void> forbidden() {
        orderService.deny();
        return R.ok();
    }

    /**
     * 系统异常兜底。
     *
     * @return 不会成功返回
     */
    @GetMapping("/boom")
    public R<Void> boom() {
        orderService.boom();
        return R.ok();
    }
}
