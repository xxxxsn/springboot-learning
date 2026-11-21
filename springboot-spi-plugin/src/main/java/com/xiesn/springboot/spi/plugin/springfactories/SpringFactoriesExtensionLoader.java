package com.xiesn.springboot.spi.plugin.springfactories;

import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 使用 {@link SpringFactoriesLoader} 加载自定义扩展。
 */
@Component
public class SpringFactoriesExtensionLoader {

    /**
     * 列出 spring.factories 中注册的 {@link PluginExtension}。
     *
     * @return 扩展列表
     */
    public List<Map<String, String>> list() {
        List<PluginExtension> extensions = SpringFactoriesLoader
                .forDefaultResourceLocation(PluginExtension.class.getClassLoader())
                .load(PluginExtension.class);
        List<Map<String, String>> result = new ArrayList<>();
        for (PluginExtension extension : extensions) {
            Map<String, String> item = new LinkedHashMap<>();
            item.put("name", extension.name());
            item.put("type", extension.getClass().getName());
            item.put("discovery", "spring-factories");
            item.put("describe", extension.describe());
            result.add(item);
        }
        return result;
    }
}
