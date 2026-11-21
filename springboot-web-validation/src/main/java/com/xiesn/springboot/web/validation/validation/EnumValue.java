package com.xiesn.springboot.web.validation.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 枚举取值校验：默认按 {@code Enum.name()} 匹配；也可指定枚举实例方法名取值。
 * <p>null 视为通过，必填请叠加 {@code @NotBlank}。</p>
 */
@Documented
@Constraint(validatedBy = EnumValueValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumValue {

    /**
     * 目标枚举类型。
     *
     * @return 枚举 Class
     */
    Class<? extends Enum<?>> value();

    /**
     * 用于取值的枚举实例方法名；空字符串表示用 {@code name()}。
     *
     * @return 方法名
     */
    String method() default "";

    /**
     * 默认错误文案。
     *
     * @return 文案
     */
    String message() default "枚举值不合法";

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
