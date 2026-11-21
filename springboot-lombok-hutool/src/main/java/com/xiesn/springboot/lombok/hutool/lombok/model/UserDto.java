package com.xiesn.springboot.lombok.hutool.lombok.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 常用 Lombok 注解组合的 DTO 示例。
 * <p>拆开写 Getter/Setter/ToString/EqualsAndHashCode，比直接 {@code @Data} 更可控。</p>
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    /**
     * 用户 ID。
     */
    private Long id;

    /**
     * 用户名。
     */
    private String name;

    /**
     * 邮箱。
     */
    private String email;
}
