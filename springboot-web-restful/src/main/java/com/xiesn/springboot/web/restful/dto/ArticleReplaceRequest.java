package com.xiesn.springboot.web.restful.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 全量替换文章请求体（PUT）。
 */
public class ArticleReplaceRequest {

    /** 标题 */
    @NotBlank(message = "不能为空")
    private String title;

    /** 正文 */
    @NotBlank(message = "不能为空")
    private String content;

    /** 状态 */
    @NotBlank(message = "不能为空")
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
