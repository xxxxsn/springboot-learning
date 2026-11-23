package com.xiesn.springboot.web.jackson.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * 订单状态。
 * <p>对外写数字码，读入时同时接受数字码、数字字符串和枚举名。</p>
 */
public enum OrderStatus {

    /** 待支付 */
    CREATED(10),

    /** 已支付 */
    PAID(20),

    /** 已关闭 */
    CLOSED(30);

    private final int code;

    /**
     * @param code 对外稳定的数字码
     */
    OrderStatus(int code) {
        this.code = code;
    }

    /**
     * 序列化成数字码，而不是枚举名。
     *
     * @return 状态码
     */
    @JsonValue
    public int code() {
        return code;
    }

    /**
     * 反序列化。数字、数字字符串、枚举名都可以。
     *
     * @param node JSON 节点
     * @return 状态；空节点返回 null
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static OrderStatus fromJson(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        if (node.isNumber()) {
            return byCode(node.intValue());
        }
        String text = node.asText();
        if (text == null || text.isBlank()) {
            return null;
        }
        for (OrderStatus status : values()) {
            if (status.name().equalsIgnoreCase(text) || Integer.toString(status.code).equals(text)) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知订单状态: " + text);
    }

    /**
     * 按数字码查找。
     *
     * @param code 状态码
     * @return 状态
     */
    private static OrderStatus byCode(int code) {
        for (OrderStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知订单状态码: " + code);
    }
}
