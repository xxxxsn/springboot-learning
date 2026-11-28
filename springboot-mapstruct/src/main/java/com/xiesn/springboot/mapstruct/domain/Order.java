package com.xiesn.springboot.mapstruct.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单实体。主键、单号、创建时间由服务端填写；金额（分）由明细汇总。
 * <p>使用可变类而不是 record：局部更新需要 setter，record 没有 {@code @MappingTarget} 可用的写入点。</p>
 */
@Getter
@Setter
@NoArgsConstructor
public class Order {

    /** 服务端主键，超过 JS 安全整数 */
    private Long id;

    /** 业务单号 */
    private String orderNo;

    /** 买家姓名 */
    private String buyerName;

    /** 订单金额，单位分 */
    private Long amountCent;

    /** 状态 */
    private OrderStatus status;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 备注，可为 null */
    private String remark;

    /** 地址，可为 null */
    private Address address;

    /** 明细 */
    private List<OrderItem> items;
}
