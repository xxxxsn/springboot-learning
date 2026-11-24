package com.xiesn.springboot.mapstruct.web;

import com.xiesn.springboot.common.result.R;
import com.xiesn.springboot.mapstruct.dto.OrderCreateRequest;
import com.xiesn.springboot.mapstruct.dto.OrderPatchRequest;
import com.xiesn.springboot.mapstruct.service.OrderMapstructService;
import com.xiesn.springboot.mapstruct.vo.OrderDetailVo;
import com.xiesn.springboot.mapstruct.vo.OrderSummaryVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 映射对照接口。响应体统一走 {@code R}，HTTP 状态保持 200，业务码看 body。
 */
@RestController
@RequestMapping("/api/mapstruct")
public class MapstructDemoController {

    private final OrderMapstructService orderMapstructService;

    /**
     * @param orderMapstructService 订单映射服务
     */
    public MapstructDemoController(OrderMapstructService orderMapstructService) {
        this.orderMapstructService = orderMapstructService;
    }

    /**
     * 一次看清样例 VO，以及 null 字段不会覆盖原值。
     *
     * @return 对照结果
     */
    @GetMapping("/overview")
    public R<Map<String, Object>> overview() {
        return R.ok(orderMapstructService.overview());
    }

    /**
     * @return 样例详情
     */
    @GetMapping("/orders/sample")
    public R<OrderDetailVo> sample() {
        return R.ok(orderMapstructService.sampleDetail());
    }

    /**
     * @return 摘要列表
     */
    @GetMapping("/orders/summaries")
    public R<List<OrderSummaryVo>> summaries() {
        return R.ok(orderMapstructService.summaries());
    }

    /**
     * @param request 创建请求
     * @return 详情
     */
    @PostMapping("/orders")
    public R<OrderDetailVo> create(@RequestBody OrderCreateRequest request) {
        return R.ok(orderMapstructService.create(request));
    }

    /**
     * @param id      主键
     * @param request 局部更新
     * @return 更新后的详情
     */
    @PatchMapping("/orders/{id}")
    public R<OrderDetailVo> patch(@PathVariable Long id, @RequestBody OrderPatchRequest request) {
        return R.ok(orderMapstructService.patch(id, request));
    }
}
