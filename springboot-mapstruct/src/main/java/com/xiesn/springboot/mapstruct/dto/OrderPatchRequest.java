package com.xiesn.springboot.mapstruct.dto;

import java.util.List;

/**
 * 局部更新入参。null 字段表示「这次不改」。
 *
 * @param buyerName  买家
 * @param statusCode 状态数字码
 * @param remark     备注
 * @param address    地址；对象本身为 null 时保留原地址，对象里的 null 字段也不覆盖
 * @param items      明细；为 null 时保留原列表，非 null 时整表替换
 */
public record OrderPatchRequest(
        String buyerName,
        Integer statusCode,
        String remark,
        AddressRequest address,
        List<OrderItemRequest> items
) {
}
