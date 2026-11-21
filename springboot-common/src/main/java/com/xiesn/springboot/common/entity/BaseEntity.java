package com.xiesn.springboot.common.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实体公共字段基类。
 * <p>具体 ORM 映射注解留给各数据访问模块添加，这里只约定字段语义。</p>
 */
public abstract class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 最后修改时间 */
    private LocalDateTime updateTime;

    /** 逻辑删除标记：0 未删除，1 已删除 */
    private Integer deleted;

    /**
     * 填充新建时的审计字段。
     */
    public void markCreated() {
        LocalDateTime now = LocalDateTime.now();
        this.createTime = now;
        this.updateTime = now;
        if (this.deleted == null) {
            this.deleted = 0;
        }
    }

    /**
     * 填充更新时的审计字段。
     */
    public void markUpdated() {
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 获取主键。
     *
     * @return 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键。
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取创建时间。
     *
     * @return 创建时间
     */
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间。
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取修改时间。
     *
     * @return 修改时间
     */
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改时间。
     *
     * @param updateTime 修改时间
     */
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取逻辑删除标记。
     *
     * @return 0 未删除，1 已删除
     */
    public Integer getDeleted() {
        return deleted;
    }

    /**
     * 设置逻辑删除标记。
     *
     * @param deleted 删除标记
     */
    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}
