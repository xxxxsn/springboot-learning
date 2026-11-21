package com.xiesn.springboot.web.exception.error;

import com.xiesn.springboot.web.exception.support.HttpStatusErrorCode;
import org.springframework.http.HttpStatus;

/**
 * 订单域错误码样板（企业按域分段：2xxxx）。
 * <p>后三位刻意对齐 HTTP，便于网关/监控按状态归类。</p>
 */
public enum OrderErrorCode implements HttpStatusErrorCode {

    /** 订单不存在 */
    ORDER_NOT_FOUND(20404, "订单不存在", HttpStatus.NOT_FOUND),

    /** 状态不允许 */
    ORDER_STATUS_INVALID(20409, "订单状态不允许此操作", HttpStatus.CONFLICT),

    /** 库存不足等业务失败 */
    ORDER_STOCK_NOT_ENOUGH(20400, "库存不足", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    /**
     * 构造。
     *
     * @param code       业务码
     * @param message    文案
     * @param httpStatus HTTP 状态
     */
    OrderErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCode() {
        return code;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
