package com.xiesn.springboot.i18n.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 登录入参。教学口令固定为 {@code secret}，只为演示错误码。
 *
 * @param username 用户名
 * @param password 密码
 */
public record LoginRequest(
        @NotBlank String username,
        @NotBlank String password
) {
}
