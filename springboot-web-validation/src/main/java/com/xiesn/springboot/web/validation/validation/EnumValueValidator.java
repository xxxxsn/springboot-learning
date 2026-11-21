package com.xiesn.springboot.web.validation.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * {@link EnumValue} 约束实现。
 */
public class EnumValueValidator implements ConstraintValidator<EnumValue, String> {

    private final Set<String> allowed = new HashSet<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(EnumValue annotation) {
        Class<? extends Enum<?>> enumType = annotation.value();
        String methodName = annotation.method();
        Enum<?>[] constants = enumType.getEnumConstants();
        if (constants == null) {
            return;
        }
        try {
            if (methodName == null || methodName.isBlank()) {
                for (Enum<?> constant : constants) {
                    allowed.add(constant.name());
                }
                return;
            }
            Method method = enumType.getMethod(methodName);
            for (Enum<?> constant : constants) {
                Object raw = method.invoke(constant);
                if (raw != null) {
                    allowed.add(String.valueOf(raw));
                }
            }
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("初始化 @EnumValue 失败: " + enumType.getName(), ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        return allowed.contains(value.trim());
    }
}
