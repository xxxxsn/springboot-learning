package com.xiesn.springboot.mapstruct.dto;

import java.math.BigDecimal;

/**
 * 明细入参。单价用元，进实体前换成整数分。
 *
 * @param skuCode   SKU 编码
 * @param skuName   SKU 名称
 * @param quantity  件数
 * @param priceYuan 单价（元），最多两位小数
 */
public record OrderItemRequest(String skuCode, String skuName, Integer quantity, BigDecimal priceYuan) {
}
