package com.xiesn.springboot.web.validation.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 中国大陆手机号校验（企业常封装的业务注解样板）。
 * <p>null 视为通过，是否必填请叠加 {@code @NotBlank}/{@code @NotNull}。</p>
 */
@Documented
@Constraint(validatedBy = MobileValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Mobile {

    /**
     * 默认错误文案。
     *
     * @return 文案
     */
    String message() default "手机号格式不正确";

    /**
     * 校验分组。
     *
     * @return 分组
     */
    Class<?>[] groups() default {};

    /**
     * 负载。
     *
     * @return 负载类型
     */
    Class<? extends Payload>[] payload() default {};
}
