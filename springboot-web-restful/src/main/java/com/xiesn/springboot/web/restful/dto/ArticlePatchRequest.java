package com.xiesn.springboot.web.restful.dto;

import jakarta.validation.constraints.Pattern;

/**
 * 部分更新文章请求体（PATCH）：字段为 null 表示不改。
 */
public class ArticlePatchRequest {

    /** 标题，可选 */
    private String title;

    /** 正文，可选 */
    private String content;

    /** 状态，可选 */
    @Pattern(regexp = "DRAFT|PUBLISHED", message = "只能是 DRAFT 或 PUBLISHED")
    private String status;

    /**
     * 获取标题。
     *
     * @return 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题。
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取正文。
     *
     * @return 正文
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置正文。
     *
     * @param content 正文
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取状态。
     *
     * @return 状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态。
     *
     * @param status 状态
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
