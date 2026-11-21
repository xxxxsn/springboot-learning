package com.xiesn.springboot.spi.plugin.springbean;

import com.xiesn.springboot.spi.plugin.api.MessagePlugin;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 收集容器中所有 {@link MessagePlugin} Bean（策略模式 / 插件注册表）。
 * <p>只包含 {@code @Component} 实现，不含纯 Java SPI 实现（那些不是 Spring Bean）。</p>
 */
@Component
public class SpringBeanPluginRegistry {

    private final Map<String, MessagePlugin> pluginsByName;

    /**
     * 注入全部 MessagePlugin Bean。
     *
     * @param plugins Spring 管理的插件
     */
    public SpringBeanPluginRegistry(List<MessagePlugin> plugins) {
        this.pluginsByName = plugins.stream()
                .filter(p -> "spring-bean".equals(p.discovery()))
                .collect(Collectors.toMap(
                        MessagePlugin::name,
                        Function.identity(),
                        (a, b) -> a,
                        LinkedHashMap::new));
    }

    /**
     * 列出 Spring Bean 插件。
     *
     * @return 插件列表
     */
    public List<Map<String, String>> list() {
        List<Map<String, String>> result = new ArrayList<>();
        for (MessagePlugin plugin : pluginsByName.values()) {
            Map<String, String> item = new LinkedHashMap<>();
            item.put("name", plugin.name());
            item.put("type", plugin.getClass().getName());
            item.put("discovery", plugin.discovery());
            result.add(item);
        }
        return result;
    }

    /**
     * 按名称调用。
     *
     * @param name    插件名
     * @param payload 内容
     * @return 结果
     */
    public Optional<String> process(String name, String payload) {
        MessagePlugin plugin = pluginsByName.get(name == null ? "" : name.toLowerCase());
        if (plugin == null) {
            return Optional.empty();
        }
        return Optional.of(plugin.process(payload));
    }
}
