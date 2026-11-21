package com.xiesn.springboot.web.exception.support;

import com.xiesn.springboot.common.exception.BusinessException;
import com.xiesn.springboot.common.result.CommonErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

/**
 * 企业级全局异常处理器（样板）。
 * <p>优先级高于 common 默认处理器；响应带 HTTP 状态 + traceId，可切换 ProblemDetail。</p>
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class EnterpriseExceptionAdvice {

    private static final Logger log = LoggerFactory.getLogger(EnterpriseExceptionAdvice.class);

    private final ErrorResponseFactory responseFactory;

    /**
     * 注入响应工厂。
     *
     * @param responseFactory 工厂
     */
    public EnterpriseExceptionAdvice(ErrorResponseFactory responseFactory) {
        this.responseFactory = responseFactory;
    }

    /**
     * 可预期业务异常。
     *
     * @param ex      业务异常
     * @param request 请求
     * @return 错误响应
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusiness(BusinessException ex, HttpServletRequest request) {
        log.warn("[exception] 业务异常 code={} message={} path={}",
                ex.getErrorCode().getCode(), ex.getMessage(), request.getRequestURI());
        return responseFactory.build(ex.getErrorCode(), ex.getMessage(), null, request);
    }

    /**
     * {@code @RequestBody} 校验失败。
     *
     * @param ex      校验异常
     * @param request 请求
     * @return 错误响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                          HttpServletRequest request) {
        return responseFactory.build(
                CommonErrorCode.BAD_REQUEST,
                "参数校验失败",
                toFieldErrors(ex.getBindingResult().getFieldErrors()),
                request);
    }

    /**
     * 表单 / 查询绑定校验失败。
     *
     * @param ex      绑定异常
     * @param request 请求
     * @return 错误响应
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> handleBind(BindException ex, HttpServletRequest request) {
        return responseFactory.build(
                CommonErrorCode.BAD_REQUEST,
                "参数校验失败",
                toFieldErrors(ex.getBindingResult().getFieldErrors()),
                request);
    }

    /**
     * 方法级约束失败。
     *
     * @param ex      约束异常
     * @param request 请求
     * @return 错误响应
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraint(ConstraintViolationException ex, HttpServletRequest request) {
        List<FieldErrorItem> errors = ex.getConstraintViolations().stream()
                .map(this::toItem)
                .toList();
        return responseFactory.build(CommonErrorCode.BAD_REQUEST, "参数校验失败", errors, request);
    }

    /**
     * JSON 不可读。
     *
     * @param ex      异常
     * @param request 请求
     * @return 错误响应
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.warn("[exception] 请求体不可读 path={} msg={}", request.getRequestURI(), ex.getMessage());
        return responseFactory.build(CommonErrorCode.BAD_REQUEST, "请求体格式错误", null, request);
    }

    /**
     * 缺少请求参数。
     *
     * @param ex      异常
     * @param request 请求
     * @return 错误响应
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingParam(MissingServletRequestParameterException ex,
                                                HttpServletRequest request) {
        FieldErrorItem item = FieldErrorItem.of(ex.getParameterName(), "不能为空");
        return responseFactory.build(CommonErrorCode.BAD_REQUEST, "缺少请求参数", List.of(item), request);
    }

    /**
     * 参数类型不匹配。
     *
     * @param ex      异常
     * @param request 请求
     * @return 错误响应
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                HttpServletRequest request) {
        FieldErrorItem item = FieldErrorItem.of(ex.getName(), "类型不匹配");
        return responseFactory.build(CommonErrorCode.BAD_REQUEST, "参数类型错误", List.of(item), request);
    }

    /**
     * HTTP 方法不支持。
     *
     * @param ex      异常
     * @param request 请求
     * @return 错误响应
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                      HttpServletRequest request) {
        return responseFactory.build(CommonErrorCode.BAD_REQUEST, "不支持的请求方法: " + ex.getMethod(), null, request);
    }

    /**
     * 无匹配 Handler（需 throw-exception-if-no-handler-found）。
     *
     * @param ex      异常
     * @param request 请求
     * @return 错误响应
     */
    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    public ResponseEntity<?> handleNotFound(Exception ex, HttpServletRequest request) {
        return responseFactory.build(CommonErrorCode.NOT_FOUND, "接口不存在", null, request);
    }

    /**
     * 兜底：不向客户端回传堆栈。
     *
     * @param ex      未知异常
     * @param request 请求
     * @return 脱敏错误响应
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnknown(Exception ex, HttpServletRequest request) {
        log.error("[exception] 未处理异常 path={}（堆栈仅日志，不回传客户端）", request.getRequestURI(), ex);
        return responseFactory.build(CommonErrorCode.INTERNAL_ERROR, "服务器内部错误", null, request);
    }

    private List<FieldErrorItem> toFieldErrors(List<FieldError> fieldErrors) {
        return fieldErrors.stream()
                .map(error -> new FieldErrorItem(error.getField(), error.getDefaultMessage(), error.getRejectedValue()))
                .toList();
    }

    private FieldErrorItem toItem(ConstraintViolation<?> violation) {
        return FieldErrorItem.of(violation.getPropertyPath().toString(), violation.getMessage());
    }
}
