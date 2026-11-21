package com.xiesn.springboot.web.validation.dto;

/**
 * 单个字段校验错误，放入 {@code R.data} 供前端高亮。
 *
 * @param field   字段路径，如 {@code address.city}
 * @param message 错误文案
 */
public record FieldErrorItem(String field, String message) {
}
