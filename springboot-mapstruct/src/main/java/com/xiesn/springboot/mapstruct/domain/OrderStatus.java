package com.xiesn.springboot.mapstruct.domain;

import com.xiesn.springboot.common.exception.BusinessException;
import com.xiesn.springboot.common.result.CommonErrorCode;

/**
 * 订单状态。对外用数字码，库存枚举名只留在服务端。
 */
public enum OrderStatus {

    /** 待支付 */
    CREATED(10, "待支付"),

    /** 已支付 */
    PAID(20, "已支付"),

    /** 已关闭 */
    CLOSED(30, "已关闭");

    private final int code;

    private final String label;

    /**
     * @param code  对外数字码
     * @param label 给 VO 的中文名
     */
    OrderStatus(int code, String label) {
        this.code = code;
        this.label = label;
    }

    /**
     * @return 数字码
     */
    public int getCode() {
        return code;
    }

    /**
     * @return 中文名
     */
    public String getLabel() {
        return label;
    }

    /**
     * 按数字码解析。未知码抛业务异常，避免静默落成 null。
     *
     * @param code 请求里的状态码
     * @return 枚举
     */
    public static OrderStatus fromCode(Integer code) {
        if (code == null) {
            throw new BusinessException(CommonErrorCode.BAD_REQUEST, "statusCode 不能为空");
        }
        for (OrderStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new BusinessException(CommonErrorCode.BAD_REQUEST, "未知订单状态: " + code);
    }
}
