package com.xiesn.springboot.web.exception.support;

/**
 * 字段级错误，供前端表单高亮。
 *
 * @param field   字段路径
 * @param message 错误文案
 * @param rejectedValue 被拒绝的值（可空，注意脱敏）
 */
public record FieldErrorItem(String field, String message, Object rejectedValue) {

    /**
     * 不含 rejectedValue 的快捷构造。
     *
     * @param field   字段
     * @param message 文案
     * @return 错误项
     */
    public static FieldErrorItem of(String field, String message) {
        return new FieldErrorItem(field, message, null);
    }
}
