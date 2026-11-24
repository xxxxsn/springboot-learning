package com.xiesn.springboot.mapstruct.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 订单明细。价格以分保存，避免用浮点做金额。
 */
@Getter
@Setter
@NoArgsConstructor
public class OrderItem {

    /** SKU 编码 */
    private String skuCode;

    /** SKU 名称 */
    private String skuName;

    /** 件数 */
    private Integer quantity;

    /** 单价，单位分 */
    private Long priceCent;
}
