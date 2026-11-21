package com.xiesn.springboot.i18n.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 文件名检查入参。真实上传应看文件头，这里只用扩展名演示错误码。
 *
 * @param filename 原始文件名
 */
public record FileCheckRequest(
        @NotBlank String filename
) {
}
