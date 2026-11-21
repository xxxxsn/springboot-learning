package com.xiesn.springboot.spi.plugin.springfactories;

/**
 * Spring Factories 扩展点契约（自定义 key，非 AutoConfiguration）。
 * <p>Boot 3 的自动配置已迁到 {@code AutoConfiguration.imports}；
 * 自定义扩展仍可用 {@code META-INF/spring.factories} + {@code SpringFactoriesLoader}。</p>
 */
public interface PluginExtension {

    /**
     * 扩展名。
     *
     * @return 名称
     */
    String name();

    /**
     * 扩展说明。
     *
     * @return 描述
     */
    String describe();
}
