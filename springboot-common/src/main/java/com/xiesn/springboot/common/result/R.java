package com.xiesn.springboot.common.result;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
import java.io.Serializable;

/**
 * 统一 API 响应包装。
 * <p>前端只约定 {@code code / message / data} 三个字段，避免各模块返回结构不一致。</p>
 *
 * @param <T> 业务数据类型
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class R<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 业务状态码，0 表示成功 */
    private int code;

    /** 提示信息 */
    private String message;

    /** 业务数据，失败时可为 null */
    private T data;

    /**
     * 供反序列化使用的无参构造。
     */
    public R() {
    }

    /**
     * 全字段构造。
     *
     * @param code    状态码
     * @param message 提示
     * @param data    数据
     */
    public R(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功且无业务数据。
     *
     * @param <T> 数据类型
     * @return 成功响应
     */
    public static <T> R<T> ok() {
        return ok(null);
    }

    /**
     * 成功并携带业务数据。
     *
     * @param data 业务数据
     * @param <T>  数据类型
     * @return 成功响应
     */
    public static <T> R<T> ok(T data) {
        return new R<>(CommonErrorCode.SUCCESS.getCode(), CommonErrorCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功并自定义提示文案。
     *
     * @param message 提示
     * @param data    业务数据
     * @param <T>     数据类型
     * @return 成功响应
     */
    public static <T> R<T> ok(String message, T data) {
        return new R<>(CommonErrorCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 按错误码失败返回。
     *
     * @param errorCode 错误码
     * @param <T>       数据类型
     * @return 失败响应
     */
    public static <T> R<T> fail(ErrorCode errorCode) {
        return new R<>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    /**
     * 按错误码失败，并覆盖默认文案。
     *
     * @param errorCode 错误码
     * @param message   自定义文案
     * @param <T>       数据类型
     * @return 失败响应
     */
    public static <T> R<T> fail(ErrorCode errorCode, String message) {
        return new R<>(errorCode.getCode(), message, null);
    }

    /**
     * 直接用数字码失败返回（兼容历史接口）。
     *
     * @param code    状态码
     * @param message 提示
     * @param <T>     数据类型
     * @return 失败响应
     */
    public static <T> R<T> fail(int code, String message) {
        return new R<>(code, message, null);
    }

    /**
     * 判断当前响应是否成功。
     *
     * @return code 为 0 时返回 true
     */
    public boolean isSuccess() {
        return this.code == CommonErrorCode.SUCCESS.getCode();
    }

    /**
     * 获取状态码。
     *
     * @return 状态码
     */
    public int getCode() {
        return code;
    }

    /**
     * 设置状态码。
     *
     * @param code 状态码
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 获取提示信息。
     *
     * @return 提示信息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置提示信息。
     *
     * @param message 提示信息
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 获取业务数据。
     *
     * @return 业务数据
     */
    public T getData() {
        return data;
    }

    /**
     * 设置业务数据。
     *
     * @param data 业务数据
     */
    public void setData(T data) {
        this.data = data;
    }
}
