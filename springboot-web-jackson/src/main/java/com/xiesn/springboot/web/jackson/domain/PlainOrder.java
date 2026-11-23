package com.xiesn.springboot.web.jackson.domain;

import java.time.LocalDateTime;

/**
 * 不加字段注解的对照订单。
 * <p>{@code status} 使用没有 {@code @JsonValue} 的枚举，序列化结果是枚举名。
 * 日期和 null 仍受 ObjectMapper 全局配置影响。</p>
 *
 * @param id         主键，保持 JSON number
 * @param createdAt  创建时间
 * @param status     默认枚举名
 * @param amountCent 金额（分）
 * @param remark     空备注
 */
public record PlainOrder(
        Long id,
        LocalDateTime createdAt,
        PlainOrderStatus status,
        Long amountCent,
        String remark
) {

    /**
     * 与企业样例对齐的对照数据。
     *
     * @return 无字段注解的订单
     */
    public static PlainOrder sample() {
        EnterpriseOrder source = EnterpriseOrder.sample();
        return new PlainOrder(
                source.id(),
                source.createdAt(),
                PlainOrderStatus.PAID,
                source.amountCent(),
                source.remark()
        );
    }

    /**
     * 对照用订单状态，保持 Jackson 默认的枚举名。
     */
    public enum PlainOrderStatus {
        /** 待支付 */
        CREATED,
        /** 已支付 */
        PAID,
        /** 已关闭 */
        CLOSED
    }
}
