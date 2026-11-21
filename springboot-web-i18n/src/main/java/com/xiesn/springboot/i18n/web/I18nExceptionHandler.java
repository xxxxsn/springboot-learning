package com.xiesn.springboot.i18n.web;

import com.xiesn.springboot.common.result.R;
import com.xiesn.springboot.i18n.error.I18nErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 把校验失败收成固定 key。
 * <p>{@code springboot-common} 的全局处理会拼出中文句子，优先级放高才能盖住它。</p>
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class I18nExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(I18nExceptionHandler.class);

    /**
     * {@code @Valid} 请求体失败。
     *
     * @param ex 校验异常
     * @return key {@code common.param_invalid}
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Void> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.info("参数校验失败 errorCount={}", ex.getBindingResult().getErrorCount());
        return R.fail(I18nErrorCode.PARAM_INVALID);
    }

    /**
     * 表单绑定失败。
     *
     * @param ex 绑定异常
     * @return key {@code common.param_invalid}
     */
    @ExceptionHandler(BindException.class)
    public R<Void> handleBindException(BindException ex) {
        log.info("参数绑定失败 errorCount={}", ex.getBindingResult().getErrorCount());
        return R.fail(I18nErrorCode.PARAM_INVALID);
    }

    /**
     * JSON 无法读取时同样视为参数不合法，避免 common 回中文「请求体格式错误」。
     *
     * @param ex 读体异常
     * @return key {@code common.param_invalid}
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public R<Void> handleUnreadable(HttpMessageNotReadableException ex) {
        log.info("请求体不可读");
        return R.fail(I18nErrorCode.PARAM_INVALID);
    }
}
