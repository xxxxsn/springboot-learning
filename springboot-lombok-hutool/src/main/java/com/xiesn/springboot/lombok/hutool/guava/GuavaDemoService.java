package com.xiesn.springboot.lombok.hutool.guava;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Guava 经典集合与工具演示（不含 RateLimiter）。
 */
@Service
public class GuavaDemoService {

    private LoadingCache<String, String> upperCache;

    /**
     * 初始化本地缓存：key 转大写。
     */
    @PostConstruct
    public void initCache() {
        upperCache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build(new CacheLoader<>() {
                    @Override
                    public String load(String key) {
                        return key.toUpperCase();
                    }
                });
    }

    /**
     * 一次返回全部样例。
     *
     * @return 汇总
     */
    public Map<String, Object> all() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("immutable", demoImmutable());
        result.put("partition", demoPartition());
        result.put("joinerSplitter", demoJoinerSplitter());
        result.put("precondition", demoPrecondition(5));
        result.put("cache", demoCache("spring"));
        return result;
    }

    /**
     * 不可变集合。
     *
     * @return 演示结果
     */
    public Map<String, Object> demoImmutable() {
        ImmutableList<String> list = ImmutableList.of("a", "b", "c");
        ImmutableMap<String, Integer> map = ImmutableMap.of("x", 1, "y", 2);
        String mutateError = null;
        try {
            list.add("d");
        } catch (UnsupportedOperationException ex) {
            mutateError = ex.getClass().getSimpleName();
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("case", "ImmutableList / ImmutableMap");
        result.put("input", List.of("a", "b", "c"));
        result.put("output", Map.of("list", list, "map", map, "mutateError", mutateError));
        result.put("pitfall", "不可变集合修改会抛 UnsupportedOperationException；对外 API 返回时优先用它防泄漏");
        return result;
    }

    /**
     * 列表分区。
     *
     * @return 演示结果
     */
    public Map<String, Object> demoPartition() {
        List<Integer> source = List.of(1, 2, 3, 4, 5, 6, 7);
        List<List<Integer>> parts = Lists.partition(source, 3);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("case", "Lists.partition");
        result.put("input", Map.of("list", source, "size", 3));
        result.put("output", parts);
        result.put("pitfall", "partition 返回的是原列表视图，改原列表会影响分区结果；批量入库常用此切段");
        return result;
    }

    /**
     * Joiner / Splitter。
     *
     * @return 演示结果
     */
    public Map<String, Object> demoJoinerSplitter() {
        String joined = Joiner.on(",").skipNulls().join("a", null, "b", "c");
        List<String> split = Splitter.on(',').trimResults().omitEmptyStrings().splitToList(" a, ,b,c ");
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("case", "Joiner / Splitter");
        result.put("input", "a,null,b,c / ' a, ,b,c '");
        result.put("output", Map.of("joined", joined, "split", split));
        result.put("pitfall", "Java 8+ String.join / 正则 split 也能做；Guava 在 skipNulls、链式配置上更顺手");
        return result;
    }

    /**
     * 前置条件校验。
     *
     * @param value 待校验值
     * @return 演示结果
     */
    public Map<String, Object> demoPrecondition(int value) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("case", "Preconditions.checkArgument");
        result.put("input", value);
        try {
            Preconditions.checkArgument(value > 0, "value 必须 > 0，实际=%s", value);
            result.put("output", Map.of("passed", true));
            result.put("pitfall", "失败抛 IllegalArgumentException；业务层更建议转成统一业务异常（见 common）");
        } catch (IllegalArgumentException ex) {
            result.put("output", Map.of("passed", false, "message", ex.getMessage()));
            result.put("pitfall", "Preconditions 适合快速参数守卫；对外 API 要映射成 R 错误码");
        }
        return result;
    }

    /**
     * 本地 LoadingCache。
     *
     * @param key 缓存键
     * @return 演示结果
     */
    public Map<String, Object> demoCache(String key) {
        String safeKey = key == null || key.isBlank() ? "spring" : key;
        String value;
        try {
            value = upperCache.get(safeKey);
        } catch (Exception ex) {
            value = "ERROR:" + ex.getMessage();
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("case", "LoadingCache");
        result.put("input", safeKey);
        result.put("output", Map.of(
                "value", value,
                "size", upperCache.size()));
        result.put("pitfall", "本地缓存无分布式一致性；多实例/强一致场景用 Redis 等（本模块只演示进程内）");
        return result;
    }
}
