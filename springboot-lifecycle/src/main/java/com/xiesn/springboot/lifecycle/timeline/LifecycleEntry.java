package com.xiesn.springboot.lifecycle.timeline;

import java.time.Instant;

/**
 * 生命周期时间线条目。
 */
public class LifecycleEntry {

    /** 自增序号，便于观察先后 */
    private final long sequence;

    /** 阶段分类，如 bootstrap / bean / runner / event / smart / shutdown */
    private final String phase;

    /** 触发组件名 */
    private final String component;

    /** 补充说明 */
    private final String detail;

    /** 记录时间 */
    private final Instant at;

    /**
     * 构造时间线条目。
     *
     * @param sequence  序号
     * @param phase     阶段
     * @param component 组件
     * @param detail    说明
     * @param at        时间
     */
    public LifecycleEntry(long sequence, String phase, String component, String detail, Instant at) {
        this.sequence = sequence;
        this.phase = phase;
        this.component = component;
        this.detail = detail;
        this.at = at;
    }

    /**
     * 获取序号。
     *
     * @return 序号
     */
    public long getSequence() {
        return sequence;
    }

    /**
     * 获取阶段。
     *
     * @return 阶段
     */
    public String getPhase() {
        return phase;
    }

    /**
     * 获取组件名。
     *
     * @return 组件名
     */
    public String getComponent() {
        return component;
    }

    /**
     * 获取说明。
     *
     * @return 说明
     */
    public String getDetail() {
        return detail;
    }

    /**
     * 获取时间。
     *
     * @return 时间
     */
    public Instant getAt() {
        return at;
    }
}
