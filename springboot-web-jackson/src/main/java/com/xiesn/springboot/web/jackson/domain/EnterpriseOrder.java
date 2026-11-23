package com.xiesn.springboot.web.jackson.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xiesn.springboot.web.jackson.support.FlexibleLongDeserializer;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 企业接口上的订单快照。
 * <p>主键单独转字符串；金额仍是数字。空的 {@code channel} 强制输出，用来对照全局 non_null。</p>
 *
 * @param id         超过 JS 安全整数的主键
 * @param createdAt  走全局 {@code yyyy-MM-dd HH:mm:ss}
 * @param paidAt     字段格式覆盖全局
 * @param legacyTime {@code java.util.Date}，吃 yml 的 date-format
 * @param status     数字码
 * @param amountCent 金额（分），保持 JSON number
 * @param remark     空则省略
 * @param channel    即使为 null 也输出
 */
public record EnterpriseOrder(
        @JsonSerialize(using = ToStringSerializer.class)
        @JsonDeserialize(using = FlexibleLongDeserializer.class)
        Long id,
        LocalDateTime createdAt,
        @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
        LocalDateTime paidAt,
        Date legacyTime,
        OrderStatus status,
        Long amountCent,
        String remark,
        @JsonInclude(JsonInclude.Include.ALWAYS)
        String channel
) {

    /** 2^53 + 1，JSON number 进 JS 后会变成 9007199254740992。 */
    public static final long SAMPLE_ID = 9007199254740993L;

    /**
     * 固定样例，测试和接口共用。
     *
     * @return 企业订单
     */
    public static EnterpriseOrder sample() {
        LocalDateTime createdAt = LocalDateTime.of(2026, 11, 23, 8, 30, 0);
        Date legacyTime = Date.from(createdAt.atZone(ZoneId.of("Asia/Shanghai")).toInstant());
        return new EnterpriseOrder(
                SAMPLE_ID,
                createdAt,
                LocalDateTime.of(2026, 11, 23, 9, 5, 0),
                legacyTime,
                OrderStatus.PAID,
                1990L,
                null,
                null
        );
    }
}
