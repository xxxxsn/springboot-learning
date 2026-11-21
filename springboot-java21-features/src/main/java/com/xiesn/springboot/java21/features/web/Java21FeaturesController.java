package com.xiesn.springboot.java21.features.web;

import com.xiesn.springboot.common.result.R;
import com.xiesn.springboot.java21.features.service.Java21DemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Java 21 特性 HTTP 入口。
 */
@RestController
@RequestMapping("/api/java21")
public class Java21FeaturesController {

    private final Java21DemoService java21DemoService;

    /**
     * 注入演示服务。
     *
     * @param java21DemoService 演示服务
     */
    public Java21FeaturesController(Java21DemoService java21DemoService) {
        this.java21DemoService = java21DemoService;
    }

    /**
     * 一次返回全部特性样例。
     *
     * @return 汇总结果
     */
    @GetMapping
    public R<Map<String, Object>> all() {
        return R.ok(java21DemoService.demoAll());
    }

    /**
     * record 演示。
     *
     * @param x 横坐标
     * @param y 纵坐标
     * @return record 样例
     */
    @GetMapping("/record")
    public R<Map<String, Object>> recordDemo(
            @RequestParam(defaultValue = "3") int x,
            @RequestParam(defaultValue = "4") int y) {
        return R.ok(java21DemoService.demoRecord(x, y));
    }

    /**
     * 密封类 + 模式匹配演示。
     *
     * @param type   形状类型
     * @param a      第一尺寸
     * @param b      第二尺寸（矩形用）
     * @return 面积结果
     */
    @GetMapping("/sealed")
    public R<Map<String, Object>> sealedDemo(
            @RequestParam(defaultValue = "circle") String type,
            @RequestParam(defaultValue = "2") double a,
            @RequestParam(defaultValue = "3") double b) {
        return R.ok(java21DemoService.demoSealedAndPattern(type, a, b));
    }

    /**
     * 文本块演示。
     *
     * @param table 表名
     * @return 文本块样例
     */
    @GetMapping("/text-block")
    public R<Map<String, Object>> textBlock(@RequestParam(defaultValue = "t_user") String table) {
        return R.ok(java21DemoService.demoTextBlock(table));
    }

    /**
     * Sequenced Collections 演示。
     *
     * @return 首尾与反转结果
     */
    @GetMapping("/sequenced")
    public R<Map<String, Object>> sequenced() {
        return R.ok(java21DemoService.demoSequencedCollection());
    }
}
