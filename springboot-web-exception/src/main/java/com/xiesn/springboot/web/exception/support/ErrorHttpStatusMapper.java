package com.xiesn.springboot.web.exception.support;

import com.xiesn.springboot.common.result.CommonErrorCode;
import com.xiesn.springboot.common.result.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * 错误码 → HTTP 状态映射。
 * <p>优先认 {@link HttpStatusErrorCode}；否则按 common 码表与常见分段约定回落。</p>
 */
@Component
public class ErrorHttpStatusMapper {

    /**
     * 映射 HTTP 状态。
     *
     * @param errorCode 错误码
     * @return HTTP 状态，未知业务失败默认 400
     */
    public HttpStatus map(ErrorCode errorCode) {
        if (errorCode instanceof HttpStatusErrorCode aware) {
            return aware.getHttpStatus();
        }
        if (errorCode instanceof CommonErrorCode common) {
            return switch (common) {
                case SUCCESS -> HttpStatus.OK;
                case BAD_REQUEST, BUSINESS_ERROR -> HttpStatus.BAD_REQUEST;
                case UNAUTHORIZED -> HttpStatus.UNAUTHORIZED;
                case FORBIDDEN -> HttpStatus.FORBIDDEN;
                case NOT_FOUND -> HttpStatus.NOT_FOUND;
                case INTERNAL_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
            };
        }
        int code = errorCode.getCode();
        // 企业常见：业务码后三位对齐 HTTP，如 20404 → 404
        int httpLike = code % 1000;
        if (httpLike >= 400 && httpLike < 600) {
            try {
                return HttpStatus.valueOf(httpLike);
            } catch (Exception ignored) {
                // fall through
            }
        }
        return HttpStatus.BAD_REQUEST;
    }
}
