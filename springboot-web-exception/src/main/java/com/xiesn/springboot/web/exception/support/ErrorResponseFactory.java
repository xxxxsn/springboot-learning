package com.xiesn.springboot.web.exception.support;

import com.xiesn.springboot.common.result.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.Instant;
import java.util.List;

/**
 * 按配置组装信封或 ProblemDetail 响应。
 */
@Component
public class ErrorResponseFactory {

    private final ExceptionProperties properties;
    private final ErrorHttpStatusMapper statusMapper;

    /**
     * 注入配置与映射器。
     *
     * @param properties   风格配置
     * @param statusMapper 状态映射
     */
    public ErrorResponseFactory(ExceptionProperties properties, ErrorHttpStatusMapper statusMapper) {
        this.properties = properties;
        this.statusMapper = statusMapper;
    }

    /**
     * 构建失败响应。
     *
     * @param errorCode 错误码
     * @param message   对外文案（可覆盖默认）
     * @param errors    字段错误，可空
     * @param request   当前请求
     * @return 响应实体
     */
    public ResponseEntity<?> build(ErrorCode errorCode,
                                   String message,
                                   List<FieldErrorItem> errors,
                                   HttpServletRequest request) {
        HttpStatus status = statusMapper.map(errorCode);
        String traceId = currentTraceId();
        String path = request == null ? null : request.getRequestURI();
        String safeMessage = message == null || message.isBlank() ? errorCode.getMessage() : message;

        if (properties.getStyle() == ExceptionProperties.Style.PROBLEM_DETAIL) {
            ProblemDetail detail = ProblemDetail.forStatusAndDetail(status, safeMessage);
            detail.setTitle(errorCode.getMessage());
            detail.setType(URI.create("https://springboot-learning.local/errors/" + errorCode.getCode()));
            detail.setProperty("code", errorCode.getCode());
            detail.setProperty("traceId", traceId);
            if (path != null) {
                detail.setInstance(URI.create(path));
            }
            if (errors != null && !errors.isEmpty()) {
                detail.setProperty("errors", errors);
            }
            return ResponseEntity.status(status).body(detail);
        }

        ErrorBody body = new ErrorBody(
                errorCode.getCode(),
                safeMessage,
                (errors == null || errors.isEmpty()) ? null : errors,
                traceId,
                path,
                Instant.now());
        return ResponseEntity.status(status).body(body);
    }

    private String currentTraceId() {
        String traceId = MDC.get(TraceConstants.TRACE_MDC);
        return traceId == null ? "" : traceId;
    }
}
