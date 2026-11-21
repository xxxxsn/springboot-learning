package com.xiesn.springboot.common.page;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * 通用分页查询入参。
 * <p>页码从 1 开始；各模块可继承本类追加业务筛选字段。</p>
 */
public class PageQuery {

    /** 默认页码 */
    public static final int DEFAULT_PAGE_NUM = 1;

    /** 默认每页条数 */
    public static final int DEFAULT_PAGE_SIZE = 10;

    /** 每页条数上限，防止一次拉爆内存 */
    public static final int MAX_PAGE_SIZE = 200;

    /** 页码，从 1 开始 */
    @Min(value = 1, message = "页码最小为 1")
    private Integer pageNum = DEFAULT_PAGE_NUM;

    /** 每页条数 */
    @Min(value = 1, message = "每页条数最小为 1")
    @Max(value = MAX_PAGE_SIZE, message = "每页条数不能超过 200")
    private Integer pageSize = DEFAULT_PAGE_SIZE;

    /**
     * 计算 SQL / 内存分页的偏移量。
     *
     * @return 从 0 开始的 offset
     */
    public long offset() {
        int safePageNum = pageNum == null || pageNum < 1 ? DEFAULT_PAGE_NUM : pageNum;
        int safePageSize = pageSize == null || pageSize < 1 ? DEFAULT_PAGE_SIZE : pageSize;
        return (long) (safePageNum - 1) * safePageSize;
    }

    /**
     * 获取安全后的每页条数。
     *
     * @return 每页条数
     */
    public int safePageSize() {
        if (pageSize == null || pageSize < 1) {
            return DEFAULT_PAGE_SIZE;
        }
        return Math.min(pageSize, MAX_PAGE_SIZE);
    }

    /**
     * 获取页码。
     *
     * @return 页码
     */
    public Integer getPageNum() {
        return pageNum;
    }

    /**
     * 设置页码。
     *
     * @param pageNum 页码
     */
    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    /**
     * 获取每页条数。
     *
     * @return 每页条数
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * 设置每页条数。
     *
     * @param pageSize 每页条数
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
