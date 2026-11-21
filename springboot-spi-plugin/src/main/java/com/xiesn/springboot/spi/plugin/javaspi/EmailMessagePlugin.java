package com.xiesn.springboot.spi.plugin.javaspi;

import com.xiesn.springboot.spi.plugin.api.MessagePlugin;

/**
 * Java SPI 邮件插件实现。
 */
public class EmailMessagePlugin implements MessagePlugin {

    /**
     * {@inheritDoc}
     */
    @Override
    public String name() {
        return "email";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String process(String payload) {
        return "[EMAIL] 已发送: " + payload;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String discovery() {
        return "java-spi";
    }
}
