package com.xiesn.springboot.common.result;

/**
 * 全项目通用错误码枚举。
 * <p>业务域错误码建议从 10000 起自行分段，避免与这里冲突。</p>
 */
public enum CommonErrorCode implements ErrorCode {

    /** 成功 */
    SUCCESS(0, "成功"),

    /** 入参不合法 */
    BAD_REQUEST(400, "请求参数错误"),

    /** 未登录或令牌失效 */
    UNAUTHORIZED(401, "未认证"),

    /** 无权限 */
    FORBIDDEN(403, "无权限"),

    /** 资源不存在 */
    NOT_FOUND(404, "资源不存在"),

    /** 服务内部异常（对外可脱敏） */
    INTERNAL_ERROR(500, "服务器内部错误"),

    /** 明确的业务失败（如库存不足） */
    BUSINESS_ERROR(1000, "业务处理失败");

    private final int code;
    private final String message;

    /**
     * 构造通用错误码。
     *
     * @param code    数字码
     * @param message 默认文案
     */
    CommonErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
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
}
