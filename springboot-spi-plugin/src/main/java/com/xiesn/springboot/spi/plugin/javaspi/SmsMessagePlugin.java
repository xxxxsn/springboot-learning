package com.xiesn.springboot.spi.plugin.javaspi;

import com.xiesn.springboot.spi.plugin.api.MessagePlugin;

/**
 * Java SPI 短信插件实现。
 * <p>通过 {@code META-INF/services/...MessagePlugin} 注册，由 {@link java.util.ServiceLoader} 加载。</p>
 */
public class SmsMessagePlugin implements MessagePlugin {

    /**
     * {@inheritDoc}
     */
    @Override
    public String name() {
        return "sms";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String process(String payload) {
        return "[SMS] 已发送: " + payload;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String discovery() {
        return "java-spi";
    }
}
