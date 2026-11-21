package com.xiesn.springboot.common.util;

import com.xiesn.springboot.common.exception.BusinessException;
import com.xiesn.springboot.common.result.CommonErrorCode;
import com.xiesn.springboot.common.result.ErrorCode;

/**
 * 业务断言工具。
 * <p>条件不满足时抛出 {@link BusinessException}，由全局异常处理器统一包装。</p>
 */
public final class AssertUtils {

    /**
     * 工具类不允许实例化。
     */
    private AssertUtils() {
    }

    /**
     * 断言表达式为真，否则抛业务异常。
     *
     * @param expression 条件
     * @param message    失败提示
     */
    public static void isTrue(boolean expression, String message) {
        isTrue(expression, CommonErrorCode.BUSINESS_ERROR, message);
    }

    /**
     * 断言表达式为真，否则按指定错误码抛异常。
     *
     * @param expression 条件
     * @param errorCode  错误码
     * @param message    失败提示
     */
    public static void isTrue(boolean expression, ErrorCode errorCode, String message) {
        if (!expression) {
            throw new BusinessException(errorCode, message);
        }
    }

    /**
     * 断言对象非 null。
     *
     * @param object  待检查对象
     * @param message 失败提示
     */
    public static void notNull(Object object, String message) {
        isTrue(object != null, CommonErrorCode.BAD_REQUEST, message);
    }

    /**
     * 断言字符串非空白。
     *
     * @param text    待检查字符串
     * @param message 失败提示
     */
    public static void hasText(String text, String message) {
        isTrue(text != null && !text.isBlank(), CommonErrorCode.BAD_REQUEST, message);
    }
}
