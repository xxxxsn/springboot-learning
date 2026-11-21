package com.xiesn.springboot.common.exception;

import com.xiesn.springboot.common.result.CommonErrorCode;
import com.xiesn.springboot.common.result.ErrorCode;

import java.io.Serial;

/**
 * 可预期的业务异常。
 * <p>由全局异常处理器捕获后转成统一 {@code R}，不要用它包装系统级故障。</p>
 */
public class BusinessException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 业务错误码 */
    private final ErrorCode errorCode;

    /**
     * 使用默认业务错误码与自定义文案。
     *
     * @param message 对外提示
     */
    public BusinessException(String message) {
        this(CommonErrorCode.BUSINESS_ERROR, message);
    }

    /**
     * 使用指定错误码（文案取错误码默认值）。
     *
     * @param errorCode 错误码
     */
    public BusinessException(ErrorCode errorCode) {
        this(errorCode, errorCode.getMessage());
    }

    /**
     * 使用指定错误码并覆盖文案。
     *
     * @param errorCode 错误码
     * @param message   对外提示
     */
    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * 获取错误码。
     *
     * @return 错误码
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
