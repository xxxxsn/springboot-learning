package com.xiesn.springboot.mapstruct.dto;

import java.util.List;

/**
 * 创建订单入参。客户端传来的 id 会被忽略。
 *
 * @param id         不可信的客户端主键
 * @param buyerName  买家
 * @param statusCode 状态数字码
 * @param remark     备注
 * @param address    地址，可以没有
 * @param items      明细；不传时按空列表
 */
public record OrderCreateRequest(
        Long id,
        String buyerName,
        Integer statusCode,
        String remark,
        AddressRequest address,
        List<OrderItemRequest> items
) {
}
