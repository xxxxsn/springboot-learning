package com.xiesn.springboot.i18n.error;

import com.xiesn.springboot.common.result.ErrorCode;

/**
 * 本模块错误码。{@code message} 是 i18n key，不是给用户看的句子。
 */
public enum I18nErrorCode implements ErrorCode {

    /** 入参不合法 */
    PARAM_INVALID(40000, "common.param_invalid"),

    /** 用户名或密码不正确 */
    LOGIN_INVALID_CREDENTIALS(41001, "auth.login.invalid_credentials"),

    /** 扩展名不在允许列表 */
    FILE_TYPE_NOT_ALLOWED(44002, "file.type_not_allowed");

    private final int code;
    private final String message;

    /**
     * @param code    数字错误码
     * @param message i18n key
     */
    I18nErrorCode(int code, String message) {
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
     * 返回 i18n key。
     *
     * @return 如 {@code auth.login.invalid_credentials}
     */
    @Override
    public String getMessage() {
        return message;
    }
}
