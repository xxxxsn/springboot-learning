package com.xiesn.springboot.web.validation.web;

import com.xiesn.springboot.common.result.CommonErrorCode;
import com.xiesn.springboot.common.result.R;
import com.xiesn.springboot.web.validation.dto.FieldErrorItem;
import com.xiesn.springboot.web.validation.support.ManualValidationDemo.ManualValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * 本模块校验异常增强：优先于 common 处理器，把全部字段错误放入 {@code R.data}。
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ValidationExceptionAdvice {

    /**
     * 处理 {@code @RequestBody} + {@code @Validated} 失败。
     *
     * @param ex 校验异常
     * @return 带字段列表的失败响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<List<FieldErrorItem>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return fail(toFieldErrors(ex.getBindingResult().getFieldErrors()));
    }

    /**
     * 处理表单 / 查询参数绑定校验失败。
     *
     * @param ex 绑定异常
     * @return 带字段列表的失败响应
     */
    @ExceptionHandler(BindException.class)
    public R<List<FieldErrorItem>> handleBindException(BindException ex) {
        return fail(toFieldErrors(ex.getBindingResult().getFieldErrors()));
    }

    /**
     * 处理方法级约束失败。
     *
     * @param ex 约束异常
     * @return 带字段列表的失败响应
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public R<List<FieldErrorItem>> handleConstraintViolation(ConstraintViolationException ex) {
        List<FieldErrorItem> errors = ex.getConstraintViolations().stream()
                .map(this::toItem)
                .toList();
        return fail(errors);
    }

    /**
     * 处理手动 Validator 失败。
     *
     * @param ex 手动校验异常
     * @return 带字段列表的失败响应
     */
    @ExceptionHandler(ManualValidationException.class)
    public R<List<FieldErrorItem>> handleManualValidation(ManualValidationException ex) {
        return fail(ex.getErrors());
    }

    private R<List<FieldErrorItem>> fail(List<FieldErrorItem> errors) {
        R<List<FieldErrorItem>> result = R.fail(CommonErrorCode.BAD_REQUEST, "参数校验失败");
        result.setData(errors);
        return result;
    }

    private List<FieldErrorItem> toFieldErrors(List<FieldError> fieldErrors) {
        return fieldErrors.stream()
                .map(error -> new FieldErrorItem(error.getField(), error.getDefaultMessage()))
                .toList();
    }

    private FieldErrorItem toItem(ConstraintViolation<?> violation) {
        return new FieldErrorItem(violation.getPropertyPath().toString(), violation.getMessage());
    }
}
