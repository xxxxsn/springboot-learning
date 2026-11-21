package com.xiesn.springboot.web.exception.service;

import com.xiesn.springboot.common.exception.BusinessException;
import com.xiesn.springboot.common.result.CommonErrorCode;
import com.xiesn.springboot.common.util.AssertUtils;
import com.xiesn.springboot.web.exception.dto.OrderCreateRequest;
import com.xiesn.springboot.web.exception.error.OrderErrorCode;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 订单异常场景演示服务。
 */
@Service
public class OrderExceptionDemoService {

    private final ConcurrentHashMap<Long, String> orders = new ConcurrentHashMap<>(Map.of(
            1001L, "PAID",
            1002L, "CANCELLED"
    ));

    /**
     * 查询订单，不存在抛域错误码。
     *
     * @param id 订单 id
     * @return 状态信息
     */
    public Map<String, Object> getOrder(Long id) {
        String status = orders.get(id);
        if (status == null) {
            throw new BusinessException(OrderErrorCode.ORDER_NOT_FOUND);
        }
        return Map.of("id", id, "status", status);
    }

    /**
     * 取消订单：已取消则冲突。
     *
     * @param id 订单 id
     * @return 结果
     */
    public Map<String, Object> cancel(Long id) {
        String status = orders.get(id);
        if (status == null) {
            throw new BusinessException(OrderErrorCode.ORDER_NOT_FOUND);
        }
        if ("CANCELLED".equals(status)) {
            throw new BusinessException(OrderErrorCode.ORDER_STATUS_INVALID, "订单已取消，无法再次取消");
        }
        orders.put(id, "CANCELLED");
        return Map.of("id", id, "status", "CANCELLED");
    }

    /**
     * 创建订单：演示断言与库存业务失败。
     *
     * @param request 入参
     * @return 结果
     */
    public Map<String, Object> create(OrderCreateRequest request) {
        AssertUtils.hasText(request.getSku(), "sku 不能为空");
        AssertUtils.notNull(request.getQuantity(), "quantity 不能为空");
        if (request.getQuantity() > 100) {
            throw new BusinessException(OrderErrorCode.ORDER_STOCK_NOT_ENOUGH);
        }
        long id = 2000L + request.getSku().hashCode() % 1000;
        orders.put(id, "CREATED");
        return Map.of("id", id, "sku", request.getSku(), "quantity", request.getQuantity(), "status", "CREATED");
    }

    /**
     * 故意抛出未包装系统异常，验证兜底脱敏。
     */
    public void boom() {
        throw new IllegalStateException("模拟底层 NPE/驱动异常，不应回传给前端");
    }

    /**
     * 无权限场景（用 common FORBIDDEN 码）。
     */
    public void deny() {
        throw new BusinessException(CommonErrorCode.FORBIDDEN, "当前用户无权操作该订单");
    }
}
