package com.xiesn.springboot.lombok.hutool.hutool;

import com.xiesn.springboot.common.result.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Hutool 演示 HTTP 入口。
 */
@RestController
@RequestMapping("/api/hutool")
@RequiredArgsConstructor
public class HutoolController {

    private final HutoolDemoService hutoolDemoService;

    /**
     * 一次返回全部样例。
     *
     * @return 汇总
     */
    @GetMapping("/all")
    public R<Map<String, Object>> all() {
        return R.ok(hutoolDemoService.all());
    }

    /**
     * 字符串/集合。
     *
     * @return 演示结果
     */
    @GetMapping("/str")
    public R<Map<String, Object>> str() {
        return R.ok(hutoolDemoService.demoStr());
    }

    /**
     * 日期。
     *
     * @return 演示结果
     */
    @GetMapping("/date")
    public R<Map<String, Object>> date() {
        return R.ok(hutoolDemoService.demoDate());
    }

    /**
     * ID。
     *
     * @return 演示结果
     */
    @GetMapping("/id")
    public R<Map<String, Object>> id() {
        return R.ok(hutoolDemoService.demoId());
    }

    /**
     * 摘要。
     *
     * @param text 原文
     * @return 演示结果
     */
    @GetMapping("/digest")
    public R<Map<String, Object>> digest(@RequestParam(defaultValue = "hello-hutool") String text) {
        return R.ok(hutoolDemoService.demoDigest(text));
    }

    /**
     * Bean 拷贝。
     *
     * @return 演示结果
     */
    @GetMapping("/bean")
    public R<Map<String, Object>> bean() {
        return R.ok(hutoolDemoService.demoBean());
    }

    /**
     * JSON。
     *
     * @return 演示结果
     */
    @GetMapping("/json")
    public R<Map<String, Object>> json() {
        return R.ok(hutoolDemoService.demoJson());
    }
}
