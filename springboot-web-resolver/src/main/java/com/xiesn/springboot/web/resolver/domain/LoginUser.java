package com.xiesn.springboot.web.resolver.domain;

/**
 * 当前请求上的登录用户。Filter 写入，解析器只负责取出。
 *
 * @param id   用户编号
 * @param name 显示名
 */
public record LoginUser(Long id, String name) {
}
