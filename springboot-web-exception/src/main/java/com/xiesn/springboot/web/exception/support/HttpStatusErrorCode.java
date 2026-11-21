package com.xiesn.springboot.web.exception.support;

import com.xiesn.springboot.common.result.ErrorCode;
import org.springframework.http.HttpStatus;

/**
 * 可声明 HTTP 状态的错误码（企业域错误码推荐实现）。
 */
public interface HttpStatusErrorCode extends ErrorCode {

    /**
     * 对应的 HTTP 状态。
     *
     * @return HTTP 状态
     */
    HttpStatus getHttpStatus();
}
