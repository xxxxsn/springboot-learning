package com.xiesn.springboot.web.validation.validation.groups;

import jakarta.validation.groups.Default;

/**
 * 创建场景校验分组。
 * <p>继承 {@link Default}，这样嵌套对象上未写 groups 的约束（如地址 {@code @NotBlank}）也会生效。</p>
 */
public interface Create extends Default {
}
