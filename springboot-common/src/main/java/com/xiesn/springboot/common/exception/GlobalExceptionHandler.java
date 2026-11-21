package com.xiesn.springboot.common.exception;

import com.xiesn.springboot.common.result.CommonErrorCode;
import com.xiesn.springboot.common.result.R;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常转换器。
 * <p>引入 {@code springboot-common} 且组件扫描能扫到本包时自动生效；
 * 各业务模块可用更具体的 {@code @RestControllerAdvice} 覆盖局部场景。</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理可预期业务异常。
     *
     * @param ex 业务异常
     * @return 统一失败响应
     */
    @ExceptionHandler(BusinessException.class)
    public R<Void> handleBusinessException(BusinessException ex) {
        log.warn("业务异常 code={} message={}", ex.getErrorCode().getCode(), ex.getMessage());
        return R.fail(ex.getErrorCode(), ex.getMessage());
    }

    /**
     * 处理 {@code @RequestBody} + {@code @Valid} 校验失败。
     *
     * @param ex 校验异常
     * @return 统一失败响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Void> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .orElse(CommonErrorCode.BAD_REQUEST.getMessage());
        return R.fail(CommonErrorCode.BAD_REQUEST, message);
    }

    /**
     * 处理表单 / 查询参数绑定校验失败。
     *
     * @param ex 绑定异常
     * @return 统一失败响应
     */
    @ExceptionHandler(BindException.class)
    public R<Void> handleBindException(BindException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .orElse(CommonErrorCode.BAD_REQUEST.getMessage());
        return R.fail(CommonErrorCode.BAD_REQUEST, message);
    }

    /**
     * 处理方法级约束（如 {@code @RequestParam @Min}）失败。
     *
     * @param ex 约束异常
     * @return 统一失败响应
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public R<Void> handleConstraintViolation(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
                .findFirst()
                .map(v -> v.getPropertyPath() + " " + v.getMessage())
                .orElse(CommonErrorCode.BAD_REQUEST.getMessage());
        return R.fail(CommonErrorCode.BAD_REQUEST, message);
    }

    /**
     * 处理 JSON 反序列化失败。
     *
     * @param ex 反序列化异常
     * @return 统一失败响应
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public R<Void> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.warn("请求体不可读: {}", ex.getMessage());
        return R.fail(CommonErrorCode.BAD_REQUEST, "请求体格式错误");
    }

    /**
     * 兜底未知异常，避免堆栈直接回传给前端。
     *
     * @param ex 未知异常
     * @return 脱敏后的失败响应
     */
    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception ex) {
        log.error("未处理异常", ex);
        return R.fail(CommonErrorCode.INTERNAL_ERROR);
    }
}
