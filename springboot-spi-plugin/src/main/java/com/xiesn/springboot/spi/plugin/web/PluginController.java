package com.xiesn.springboot.spi.plugin.web;

import com.xiesn.springboot.common.result.R;
import com.xiesn.springboot.spi.plugin.service.PluginDemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * SPI / 插件化观测接口。
 */
@RestController
@RequestMapping("/api/spi")
public class PluginController {

    private final PluginDemoService pluginDemoService;

    /**
     * 注入演示服务。
     *
     * @param pluginDemoService 演示服务
     */
    public PluginController(PluginDemoService pluginDemoService) {
        this.pluginDemoService = pluginDemoService;
    }

    /**
     * 三类发现机制总览。
     *
     * @return 汇总
     */
    @GetMapping("/overview")
    public R<Map<String, Object>> overview() {
        return R.ok(pluginDemoService.overview());
    }

    /**
     * 调用 Java SPI 插件。
     *
     * @param name    sms / email
     * @param payload 消息内容
     * @return 调用结果
     */
    @GetMapping("/java/{name}")
    public R<Map<String, Object>> invokeJava(
            @PathVariable String name,
            @RequestParam(defaultValue = "hello-spi") String payload) {
        return R.ok(pluginDemoService.invokeJavaSpi(name, payload));
    }

    /**
     * 调用 Spring Bean 插件。
     *
     * @param name    push / wechat
     * @param payload 消息内容
     * @return 调用结果
     */
    @GetMapping("/spring/{name}")
    public R<Map<String, Object>> invokeSpring(
            @PathVariable String name,
            @RequestParam(defaultValue = "hello-spi") String payload) {
        return R.ok(pluginDemoService.invokeSpringBean(name, payload));
    }
}
