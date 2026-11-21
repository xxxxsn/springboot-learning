package com.xiesn.springboot.lombok.hutool.lombok;

import com.xiesn.springboot.common.result.R;
import com.xiesn.springboot.lombok.hutool.lombok.model.BuilderUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Lombok 演示 HTTP 入口。
 * <p>使用 {@code @RequiredArgsConstructor} 生成构造注入，与显式构造等价。</p>
 */
@RestController
@RequestMapping("/api/lombok")
@RequiredArgsConstructor
public class LombokController {

    private final LombokDemoService lombokDemoService;

    /**
     * 常用注解汇总。
     *
     * @return 演示结果
     */
    @GetMapping("/overview")
    public R<Map<String, Object>> overview() {
        return R.ok(lombokDemoService.overview());
    }

    /**
     * Builder + Jackson 反序列化（成功路径用已修好的模型）。
     *
     * @param user 请求体
     * @return 演示结果
     */
    @PostMapping("/jackson")
    public R<Map<String, Object>> jackson(@RequestBody BuilderUser user) {
        return R.ok(lombokDemoService.jackson(user));
    }

    /**
     * equals/hashCode 坑点对比。
     *
     * @return 演示结果
     */
    @GetMapping("/equals")
    public R<Map<String, Object>> equalsDemo() {
        return R.ok(lombokDemoService.equalsDemo());
    }
}
