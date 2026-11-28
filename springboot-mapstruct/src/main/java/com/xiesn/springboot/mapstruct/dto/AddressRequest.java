package com.xiesn.springboot.mapstruct.dto;

/**
 * 收货地址入参。
 *
 * @param province 省，局部更新时 null 表示不改
 * @param city     市
 * @param street   街道门牌
 */
public record AddressRequest(String province, String city, String street) {
}
