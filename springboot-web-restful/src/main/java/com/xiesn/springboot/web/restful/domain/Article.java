package com.xiesn.springboot.web.restful.domain;

import java.time.Instant;

/**
 * 文章资源（内存演示模型）。
 *
 * @param id        主键
 * @param title     标题
 * @param content   正文
 * @param status    状态：DRAFT / PUBLISHED
 * @param updatedAt 最近更新时间
 */
public record Article(
        Long id,
        String title,
        String content,
        String status,
        Instant updatedAt
) {
}
