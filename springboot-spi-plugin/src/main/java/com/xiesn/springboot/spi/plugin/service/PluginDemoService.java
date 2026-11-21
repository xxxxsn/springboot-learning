package com.xiesn.springboot.spi.plugin.service;

import com.xiesn.springboot.common.exception.BusinessException;
import com.xiesn.springboot.common.result.CommonErrorCode;
import com.xiesn.springboot.spi.plugin.javaspi.JavaSpiPluginLoader;
import com.xiesn.springboot.spi.plugin.springbean.SpringBeanPluginRegistry;
import com.xiesn.springboot.spi.plugin.springfactories.SpringFactoriesExtensionLoader;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 汇总三种插件发现机制，供 HTTP 查阅与调用。
 */
@Service
public class PluginDemoService {

    private final JavaSpiPluginLoader javaSpiPluginLoader;
    private final SpringBeanPluginRegistry springBeanPluginRegistry;
    private final SpringFactoriesExtensionLoader springFactoriesExtensionLoader;

    /**
     * 注入三类加载器。
     *
     * @param javaSpiPluginLoader            Java SPI
     * @param springBeanPluginRegistry       Spring Bean 注册表
     * @param springFactoriesExtensionLoader SpringFactoriesLoader
     */
    public PluginDemoService(JavaSpiPluginLoader javaSpiPluginLoader,
                             SpringBeanPluginRegistry springBeanPluginRegistry,
                             SpringFactoriesExtensionLoader springFactoriesExtensionLoader) {
        this.javaSpiPluginLoader = javaSpiPluginLoader;
        this.springBeanPluginRegistry = springBeanPluginRegistry;
        this.springFactoriesExtensionLoader = springFactoriesExtensionLoader;
    }

    /**
     * 总览：三类发现机制各自有哪些插件/扩展。
     *
     * @return 汇总
     */
    public Map<String, Object> overview() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("javaSpi", javaSpiPluginLoader.list());
        result.put("springBean", springBeanPluginRegistry.list());
        result.put("springFactories", springFactoriesExtensionLoader.list());
        result.put("hint", Map.of(
                "javaSpi", "META-INF/services + ServiceLoader，实现类通常不是 Spring Bean",
                "springBean", "@Component 实现 + 构造注入 List<接口>，生命周期由容器管理",
                "springFactories", "META-INF/spring.factories 自定义 key；AutoConfiguration 请用 AutoConfiguration.imports（见 #2 hello）",
                "pf4j", "独立进程/热插拔 jar 见百科 #415 springboot-pf4j"));
        return result;
    }

    /**
     * 调用 Java SPI 插件。
     *
     * @param name    插件名 sms/email
     * @param payload 内容
     * @return 调用结果
     */
    public Map<String, Object> invokeJavaSpi(String name, String payload) {
        String result = javaSpiPluginLoader.process(name, payload)
                .orElseThrow(() -> new BusinessException(
                        CommonErrorCode.NOT_FOUND,
                        "Java SPI 插件不存在: " + name));
        return invokeBody("java-spi", name, payload, result);
    }

    /**
     * 调用 Spring Bean 插件。
     *
     * @param name    插件名 push/wechat
     * @param payload 内容
     * @return 调用结果
     */
    public Map<String, Object> invokeSpringBean(String name, String payload) {
        String result = springBeanPluginRegistry.process(name, payload)
                .orElseThrow(() -> new BusinessException(
                        CommonErrorCode.NOT_FOUND,
                        "Spring Bean 插件不存在: " + name));
        return invokeBody("spring-bean", name, payload, result);
    }

    private Map<String, Object> invokeBody(String discovery, String name, String payload, String output) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("discovery", discovery);
        body.put("name", name);
        body.put("payload", payload);
        body.put("output", output);
        return body;
    }
}
