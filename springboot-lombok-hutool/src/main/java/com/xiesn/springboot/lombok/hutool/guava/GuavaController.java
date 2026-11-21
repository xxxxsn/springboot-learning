package com.xiesn.springboot.lombok.hutool.guava;

import com.xiesn.springboot.common.result.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Guava 演示 HTTP 入口。
 */
@RestController
@RequestMapping("/api/guava")
@RequiredArgsConstructor
public class GuavaController {

    private final GuavaDemoService guavaDemoService;

    /**
     * 一次返回全部样例。
     *
     * @return 汇总
     */
    @GetMapping("/all")
    public R<Map<String, Object>> all() {
        return R.ok(guavaDemoService.all());
    }

    /**
     * 不可变集合。
     *
     * @return 演示结果
     */
    @GetMapping("/immutable")
    public R<Map<String, Object>> immutable() {
        return R.ok(guavaDemoService.demoImmutable());
    }

    /**
     * 列表分区。
     *
     * @return 演示结果
     */
    @GetMapping("/partition")
    public R<Map<String, Object>> partition() {
        return R.ok(guavaDemoService.demoPartition());
    }

    /**
     * Joiner / Splitter。
     *
     * @return 演示结果
     */
    @GetMapping("/joiner")
    public R<Map<String, Object>> joiner() {
        return R.ok(guavaDemoService.demoJoinerSplitter());
    }

    /**
     * 前置条件。
     *
     * @param value 待校验值
     * @return 演示结果
     */
    @GetMapping("/precondition")
    public R<Map<String, Object>> precondition(@RequestParam(defaultValue = "5") int value) {
        return R.ok(guavaDemoService.demoPrecondition(value));
    }

    /**
     * 本地缓存。
     *
     * @param key 键
     * @return 演示结果
     */
    @GetMapping("/cache")
    public R<Map<String, Object>> cache(@RequestParam(defaultValue = "spring") String key) {
        return R.ok(guavaDemoService.demoCache(key));
    }
}
