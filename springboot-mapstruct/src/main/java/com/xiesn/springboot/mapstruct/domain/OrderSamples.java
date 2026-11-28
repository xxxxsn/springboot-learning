package com.xiesn.springboot.mapstruct.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 固定样例，接口和测试共用。主键取 2^53 + 1，用来对照 VO 把 Long 写成字符串。
 */
public final class OrderSamples {

    /** 2^53 + 1。JSON number 进 JS 后会变成 9007199254740992。 */
    public static final long SAMPLE_ID = 9007199254740993L;

    private OrderSamples() {
    }

    /**
     * @return 一笔已支付、带地址和一条明细的订单
     */
    public static Order storedSample() {
        Address address = new Address();
        address.setProvince("浙江省");
        address.setCity("杭州市");
        address.setStreet("文三路 1 号");

        OrderItem item = new OrderItem();
        item.setSkuCode("SKU-TEA");
        item.setSkuName("龙井");
        item.setQuantity(2);
        item.setPriceCent(1990L);

        List<OrderItem> items = new ArrayList<>();
        items.add(item);

        Order order = new Order();
        order.setId(SAMPLE_ID);
        order.setOrderNo("ORD-" + SAMPLE_ID);
        order.setBuyerName("张三");
        order.setAmountCent(3980L);
        order.setStatus(OrderStatus.PAID);
        order.setCreatedAt(LocalDateTime.of(2026, 11, 24, 8, 30, 0));
        order.setRemark(null);
        order.setAddress(address);
        order.setItems(items);
        return order;
    }
}
