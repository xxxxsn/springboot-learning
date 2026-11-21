package com.xiesn.springboot.hello.web;

import com.xiesn.springboot.common.exception.BusinessException;
import com.xiesn.springboot.common.result.R;
import com.xiesn.springboot.common.util.AssertUtils;
import com.xiesn.springboot.hello.autoconfigure.HelloProperties;
import com.xiesn.springboot.hello.autoconfigure.HelloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Hello HTTP 接口。
 * <p>演示统一返回、自动配置 Bean 注入，以及业务异常如何被 common 全局处理。</p>
 */
@RestController
@RequestMapping("/api/hello")
public class HelloController {

    private final HelloService helloService;

    /**
     * 注入由自动配置创建的 HelloService。
     *
     * @param helloService Hello 服务
     */
    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    /**
     * 问候接口。
     *
     * @param name 可选称呼
     * @return 统一成功响应
     */
    @GetMapping
    public R<Map<String, Object>> hello(@RequestParam(required = false) String name) {
        String message = helloService.greet(name);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("message", message);
        payload.put("tip", "本接口返回结构来自 springboot-common 的 R");
        return R.ok(payload);
    }

    /**
     * 查看当前自动配置生效的属性，便于对照 application.yml。
     *
     * @return 配置快照
     */
    @GetMapping("/config")
    public R<HelloProperties> config() {
        return R.ok(helloService.currentProperties());
    }

    /**
     * 故意触发业务异常，验证 GlobalExceptionHandler。
     *
     * @param keyword 必须等于 ok，否则抛错
     * @return 成功响应
     */
    @GetMapping("/boom")
    public R<String> boom(@RequestParam String keyword) {
        AssertUtils.hasText(keyword, "keyword 不能为空");
        if (!"ok".equalsIgnoreCase(keyword)) {
            throw new BusinessException("keyword 必须为 ok，当前=" + keyword);
        }
        return R.ok("校验通过");
    }
}
