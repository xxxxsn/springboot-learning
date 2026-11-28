package com.xiesn.springboot.mapstruct.service;

import com.xiesn.springboot.common.exception.BusinessException;
import com.xiesn.springboot.common.result.CommonErrorCode;
import com.xiesn.springboot.mapstruct.domain.Order;
import com.xiesn.springboot.mapstruct.domain.OrderSamples;
import com.xiesn.springboot.mapstruct.dto.OrderCreateRequest;
import com.xiesn.springboot.mapstruct.dto.OrderPatchRequest;
import com.xiesn.springboot.mapstruct.mapper.OrderMapper;
import com.xiesn.springboot.mapstruct.vo.OrderDetailVo;
import com.xiesn.springboot.mapstruct.vo.OrderSummaryVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 内存订单。映射交给 MapStruct，这里只补服务端才知道的主键、单号和创建时间。
 */
@Service
public class OrderMapstructService {

    private static final Logger log = LoggerFactory.getLogger(OrderMapstructService.class);

    private final OrderMapper orderMapper;

    private final Map<Long, Order> orders = new ConcurrentHashMap<>();

    /** 新建单的主键从这个值递增，和样例主键错开，且仍大于 JS 安全整数。 */
    private final AtomicLong idSequence = new AtomicLong(9_007_199_254_741_000L);

    /**
     * @param orderMapper 编译期生成的映射器
     */
    public OrderMapstructService(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
        orders.put(OrderSamples.SAMPLE_ID, OrderSamples.storedSample());
    }

    /**
     * 总览：样例详情，以及一次不落库的局部更新预览。
     *
     * @return 对照数据
     */
    public Map<String, Object> overview() {
        Order stored = required(OrderSamples.SAMPLE_ID);
        Order draft = orderMapper.copy(stored);
        orderMapper.update(new OrderPatchRequest(null, 30, "只改备注", null, null), draft);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("port", 8030);
        data.put("mapper", orderMapper.getClass().getName());
        data.put("sampleId", String.valueOf(OrderSamples.SAMPLE_ID));
        data.put("strategies", List.of(
                "componentModel=spring，编译生成 OrderMapperImpl 并构造器注入",
                "同名属性自动映射；改名或类型不同才写 @Mapping",
                "嵌套地址、明细集合按元素方法递归",
                "分/元、状态码/枚举、Long id 转 String、LocalDateTime 转文本",
                "创建时忽略客户端 id，金额在映射后按明细重算",
                "更新使用 IGNORE：null 不覆盖；地址按字段更新；明细不走集合的 clear/addAll，请求带了才整表替换",
                "unmappedTargetPolicy=ERROR，漏写目标字段会编译失败"
        ));
        data.put("sampleDetail", orderMapper.toDetail(stored));
        data.put("sampleSummary", orderMapper.toSummary(stored));
        data.put("afterPatchPreview", orderMapper.toDetail(draft));
        return data;
    }

    /**
     * @return 样例详情
     */
    public OrderDetailVo sampleDetail() {
        return orderMapper.toDetail(required(OrderSamples.SAMPLE_ID));
    }

    /**
     * @return 当前内存里的全部摘要
     */
    public List<OrderSummaryVo> summaries() {
        return orderMapper.toSummaries(new ArrayList<>(orders.values()));
    }

    /**
     * @param request 创建请求
     * @return 详情
     */
    public OrderDetailVo create(OrderCreateRequest request) {
        Order order = orderMapper.toEntity(request);
        long id = idSequence.incrementAndGet();
        order.setId(id);
        order.setOrderNo("ORD-" + id);
        order.setCreatedAt(LocalDateTime.now().withNano(0));
        orders.put(id, order);
        int itemCount = order.getItems() == null ? 0 : order.getItems().size();
        log.info("[mapstruct] create orderNo={} status={} itemCount={} amountCent={}",
                order.getOrderNo(), order.getStatus(), itemCount, order.getAmountCent());
        return orderMapper.toDetail(order);
    }

    /**
     * @param id      主键
     * @param request 局部更新
     * @return 更新后的详情
     */
    public OrderDetailVo patch(Long id, OrderPatchRequest request) {
        Order order = required(id);
        synchronized (order) {
            orderMapper.update(request, order);
        }
        log.info("[mapstruct] patch id={} status={} amountCent={}", id, order.getStatus(), order.getAmountCent());
        return orderMapper.toDetail(order);
    }

    /**
     * @param id 主键
     * @return 已存在的订单
     */
    private Order required(Long id) {
        Order order = orders.get(id);
        if (order == null) {
            throw new BusinessException(CommonErrorCode.NOT_FOUND, "订单不存在");
        }
        return order;
    }
}
