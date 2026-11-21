package com.xiesn.springboot.web.exception.support;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;

/**
 * 企业统一错误响应体（信封模式）。
 * <p>相对 common {@code R} 增加 traceId / path / timestamp，并把字段错误放进 {@code errors}。</p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorBody {

    /** 业务错误码 */
    private int code;

    /** 对外提示 */
    private String message;

    /** 字段错误列表 */
    private List<FieldErrorItem> errors;

    /** 链路 ID */
    private String traceId;

    /** 请求路径 */
    private String path;

    /** 时间戳 */
    private Instant timestamp;

    /**
     * 无参构造。
     */
    public ErrorBody() {
    }

    /**
     * 全字段构造。
     *
     * @param code      错误码
     * @param message   提示
     * @param errors    字段错误
     * @param traceId   链路 ID
     * @param path      路径
     * @param timestamp 时间
     */
    public ErrorBody(int code, String message, List<FieldErrorItem> errors,
                     String traceId, String path, Instant timestamp) {
        this.code = code;
        this.message = message;
        this.errors = errors;
        this.traceId = traceId;
        this.path = path;
        this.timestamp = timestamp;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FieldErrorItem> getErrors() {
        return errors;
    }

    public void setErrors(List<FieldErrorItem> errors) {
        this.errors = errors;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
