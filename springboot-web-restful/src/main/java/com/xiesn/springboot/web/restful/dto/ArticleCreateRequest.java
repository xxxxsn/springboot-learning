package com.xiesn.springboot.web.restful.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 创建文章请求体。
 */
public class ArticleCreateRequest {

    /** 标题 */
    @NotBlank(message = "不能为空")
    private String title;

    /** 正文 */
    @NotBlank(message = "不能为空")
    private String content;

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
}
