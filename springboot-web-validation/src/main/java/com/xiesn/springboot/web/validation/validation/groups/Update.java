package com.xiesn.springboot.web.validation.validation.groups;

import jakarta.validation.groups.Default;

/**
 * 更新场景校验分组。
 * <p>继承 {@link Default}，嵌套对象默认组约束一并生效。</p>
 */
public interface Update extends Default {
}
