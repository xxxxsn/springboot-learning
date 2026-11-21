package com.xiesn.springboot.lombok.hutool.lombok.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 已修好的 Builder 模型：同时提供无参/全参，Jackson 反序列化可用。
 * <p>坑：只写 {@code @Builder} 时 Lombok 默认生成全参私有构造，没有无参，
 * Jackson 反序列化会失败。正确做法是补 {@code @NoArgsConstructor} + {@code @AllArgsConstructor}，
 * 或使用 {@code @Jacksonized @Builder}。</p>
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuilderUser {

    /**
     * 用户名。
     */
    private String username;

    /**
     * 年龄。
     */
    private Integer age;
}
