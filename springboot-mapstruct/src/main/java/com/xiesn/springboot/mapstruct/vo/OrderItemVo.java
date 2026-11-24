package com.xiesn.springboot.mapstruct.vo;

import java.math.BigDecimal;

/**
 * 明细出参。单价回到元。
 *
 * @param skuCode   SKU 编码
 * @param skuName   SKU 名称
 * @param quantity  件数
 * @param priceYuan 单价（元）
 */
public record OrderItemVo(String skuCode, String skuName, Integer quantity, BigDecimal priceYuan) {
}
