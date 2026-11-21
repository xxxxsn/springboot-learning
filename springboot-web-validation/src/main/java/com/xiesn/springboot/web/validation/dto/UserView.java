package com.xiesn.springboot.web.validation.dto;

/**
 * 用户保存成功后的回显。
 *
 * @param id       主键
 * @param username 用户名
 * @param mobile   手机号
 * @param status   状态
 * @param city     城市
 */
public record UserView(
        Long id,
        String username,
        String mobile,
        String status,
        String city
) {
}
