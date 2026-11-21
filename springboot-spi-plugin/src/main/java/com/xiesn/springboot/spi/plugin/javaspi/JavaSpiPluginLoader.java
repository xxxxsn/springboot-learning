package com.xiesn.springboot.spi.plugin.javaspi;

import com.xiesn.springboot.spi.plugin.api.MessagePlugin;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;

/**
 * 用 {@link ServiceLoader} 加载 Java SPI 插件。
 * <p>注意：每次 {@code load} 都会新建实现实例；生产中通常缓存，或改用 Spring 管理生命周期。</p>
 */
@Component
public class JavaSpiPluginLoader {

    /**
     * 列出全部 Java SPI 插件。
     *
     * @return 插件快照
     */
    public List<Map<String, String>> list() {
        List<Map<String, String>> result = new ArrayList<>();
        for (MessagePlugin plugin : ServiceLoader.load(MessagePlugin.class)) {
            Map<String, String> item = new LinkedHashMap<>();
            item.put("name", plugin.name());
            item.put("type", plugin.getClass().getName());
            item.put("discovery", plugin.discovery());
            result.add(item);
        }
        return result;
    }

    /**
     * 按名称查找插件并处理。
     *
     * @param name    插件名
     * @param payload 内容
     * @return 处理结果；找不到则 empty
     */
    public Optional<String> process(String name, String payload) {
        for (MessagePlugin plugin : ServiceLoader.load(MessagePlugin.class)) {
            if (plugin.name().equalsIgnoreCase(name)) {
                return Optional.of(plugin.process(payload));
            }
        }
        return Optional.empty();
    }
}
