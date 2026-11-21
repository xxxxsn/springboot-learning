package com.xiesn.springboot.spi.plugin.springfactories;

/**
 * 审计扩展：由 spring.factories 声明，SpringFactoriesLoader 加载。
 */
public class AuditPluginExtension implements PluginExtension {

    /**
     * {@inheritDoc}
     */
    @Override
    public String name() {
        return "audit";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String describe() {
        return "审计扩展：记录插件调用轨迹（演示）";
    }
}
