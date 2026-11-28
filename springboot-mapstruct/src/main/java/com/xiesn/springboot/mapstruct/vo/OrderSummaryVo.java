package com.xiesn.springboot.mapstruct.vo;

import java.math.BigDecimal;

/**
 * 列表摘要。比实体少地址和明细，多出来的源字段允许不映射。
 *
 * @param id         主键字符串
 * @param orderNo    业务单号
 * @param buyerName  买家
 * @param amountYuan 订单金额（元）
 * @param statusCode 状态码
 */
public record OrderSummaryVo(
        String id,
        String orderNo,
        String buyerName,
        BigDecimal amountYuan,
        Integer statusCode
) {
}
