package com.xiesn.springboot.mapstruct.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单详情。id 用字符串，避免雪花主键进浏览器丢精度。
 *
 * @param id            主键字符串
 * @param orderNo       业务单号
 * @param buyerName     买家
 * @param amountYuan    订单金额（元）
 * @param statusCode    状态码
 * @param statusLabel   状态中文
 * @param createdAt     创建时间，JSON 形态交给 Jackson（默认 ISO-8601）
 * @param createdAtText MapStruct 按 pattern 格式化的文本，用来对照 #26
 * @param remark        备注
 * @param address       地址
 * @param items         明细
 * @param itemCount     明细条数
 */
public record OrderDetailVo(
        String id,
        String orderNo,
        String buyerName,
        BigDecimal amountYuan,
        Integer statusCode,
        String statusLabel,
        LocalDateTime createdAt,
        String createdAtText,
        String remark,
        AddressVo address,
        List<OrderItemVo> items,
        int itemCount
) {
}
