package com.xiesn.springboot.mapstruct.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 收货地址。可变 Bean，才能被 {@code @MappingTarget} 按字段局部更新。
 */
@Getter
@Setter
@NoArgsConstructor
public class Address {

    /** 省 */
    private String province;

    /** 市 */
    private String city;

    /** 街道门牌 */
    private String street;
}
