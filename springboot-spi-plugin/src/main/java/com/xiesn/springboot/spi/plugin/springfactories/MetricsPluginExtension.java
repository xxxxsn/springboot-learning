package com.xiesn.springboot.spi.plugin.springfactories;

/**
 * 指标扩展：由 spring.factories 声明。
 */
public class MetricsPluginExtension implements PluginExtension {

    /**
     * {@inheritDoc}
     */
    @Override
    public String name() {
        return "metrics";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String describe() {
        return "指标扩展：统计插件调用次数（演示）";
    }
}
