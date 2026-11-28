package com.xiesn.springboot.mapstruct.vo;

/**
 * 地址出参。fullAddress 由省市区拼出来，库里不存这一列。
 *
 * @param province    省
 * @param city        市
 * @param street      街道
 * @param fullAddress 拼接后的完整地址
 */
public record AddressVo(String province, String city, String street, String fullAddress) {
}
