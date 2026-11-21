package com.xiesn.springboot.common.page;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 通用分页结果。
 *
 * @param <T> 行数据类型
 */
public class PageResult<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 当前页码 */
    private long pageNum;

    /** 每页条数 */
    private long pageSize;

    /** 总记录数 */
    private long total;

    /** 当前页数据 */
    private List<T> records;

    /**
     * 无参构造。
     */
    public PageResult() {
    }

    /**
     * 全字段构造。
     *
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @param total    总记录数
     * @param records  当前页数据
     */
    public PageResult(long pageNum, long pageSize, long total, List<T> records) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.records = records;
    }

    /**
     * 根据查询条件与数据构建分页结果。
     *
     * @param query   分页入参
     * @param total   总记录数
     * @param records 当前页数据
     * @param <T>     行类型
     * @return 分页结果
     */
    public static <T> PageResult<T> of(PageQuery query, long total, List<T> records) {
        long pageNum = query.getPageNum() == null ? PageQuery.DEFAULT_PAGE_NUM : query.getPageNum();
        long pageSize = query.safePageSize();
        List<T> safeRecords = records == null ? Collections.emptyList() : records;
        return new PageResult<>(pageNum, pageSize, total, safeRecords);
    }

    /**
     * 空分页结果。
     *
     * @param query 分页入参
     * @param <T>   行类型
     * @return 空结果
     */
    public static <T> PageResult<T> empty(PageQuery query) {
        return of(query, 0L, Collections.emptyList());
    }

    /**
     * 计算总页数。
     *
     * @return 总页数，至少为 0
     */
    public long getPages() {
        if (pageSize <= 0) {
            return 0L;
        }
        return (total + pageSize - 1) / pageSize;
    }

    /**
     * 获取页码。
     *
     * @return 页码
     */
    public long getPageNum() {
        return pageNum;
    }

    /**
     * 设置页码。
     *
     * @param pageNum 页码
     */
    public void setPageNum(long pageNum) {
        this.pageNum = pageNum;
    }

    /**
     * 获取每页条数。
     *
     * @return 每页条数
     */
    public long getPageSize() {
        return pageSize;
    }

    /**
     * 设置每页条数。
     *
     * @param pageSize 每页条数
     */
    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 获取总记录数。
     *
     * @return 总记录数
     */
    public long getTotal() {
        return total;
    }

    /**
     * 设置总记录数。
     *
     * @param total 总记录数
     */
    public void setTotal(long total) {
        this.total = total;
    }

    /**
     * 获取当前页数据。
     *
     * @return 数据列表
     */
    public List<T> getRecords() {
        return records;
    }

    /**
     * 设置当前页数据。
     *
     * @param records 数据列表
     */
    public void setRecords(List<T> records) {
        this.records = records;
    }
}
