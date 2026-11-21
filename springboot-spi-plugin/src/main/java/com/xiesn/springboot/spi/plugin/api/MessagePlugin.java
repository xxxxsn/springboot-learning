package com.xiesn.springboot.spi.plugin.api;

/**
 * 消息插件契约。
 * <p>同一接口可被 Java SPI（{@code META-INF/services}）或 Spring（{@code @Component}）发现。</p>
 */
public interface MessagePlugin {

    /**
     * 插件唯一名称（如 sms / email）。
     *
     * @return 名称
     */
    String name();

    /**
     * 处理消息。
     *
     * @param payload 原文
     * @return 处理结果说明
     */
    String process(String payload);

    /**
     * 发现来源标记，便于对照。
     *
     * @return java-spi / spring-bean / spring-factories
     */
    String discovery();
}
