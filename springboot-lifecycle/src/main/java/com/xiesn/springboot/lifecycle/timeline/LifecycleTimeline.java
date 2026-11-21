package com.xiesn.springboot.lifecycle.timeline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 生命周期时间线收集器。
 * <p>使用静态存储，以便 {@code SpringApplicationRunListener} /
 * {@code ApplicationContextInitializer} 在 Spring 容器创建前也能写入。</p>
 */
public final class LifecycleTimeline {

    private static final Logger log = LoggerFactory.getLogger(LifecycleTimeline.class);

    private static final AtomicLong SEQUENCE = new AtomicLong(0);

    private static final List<LifecycleEntry> ENTRIES = new ArrayList<>();

    /**
     * 工具类不允许实例化。
     */
    private LifecycleTimeline() {
    }

    /**
     * 追加一条时间线记录。
     *
     * @param phase     阶段分类
     * @param component 组件名
     * @param detail    说明
     */
    public static void record(String phase, String component, String detail) {
        LifecycleEntry entry = new LifecycleEntry(
                SEQUENCE.incrementAndGet(),
                phase,
                component,
                detail,
                Instant.now());
        synchronized (ENTRIES) {
            ENTRIES.add(entry);
        }
        log.info("[lifecycle] seq={} phase={} component={} detail={}",
                entry.getSequence(), phase, component, detail);
    }

    /**
     * 获取时间线快照（副本）。
     *
     * @return 条目列表
     */
    public static List<LifecycleEntry> snapshot() {
        synchronized (ENTRIES) {
            return List.copyOf(ENTRIES);
        }
    }

    /**
     * 按 phase 分组汇总。
     *
     * @return phase → 条目列表
     */
    public static Map<String, List<LifecycleEntry>> summaryByPhase() {
        return snapshot().stream()
                .collect(Collectors.groupingBy(
                        LifecycleEntry::getPhase,
                        LinkedHashMap::new,
                        Collectors.toList()));
    }

    /**
     * 清空时间线（测试用）。
     */
    public static void clear() {
        synchronized (ENTRIES) {
            ENTRIES.clear();
        }
        SEQUENCE.set(0);
    }
}
