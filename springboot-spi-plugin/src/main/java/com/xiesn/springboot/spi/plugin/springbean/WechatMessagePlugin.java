package com.xiesn.springboot.spi.plugin.springbean;

import com.xiesn.springboot.spi.plugin.api.MessagePlugin;
import org.springframework.stereotype.Component;

/**
 * Spring Bean 企微插件。
 */
@Component
public class WechatMessagePlugin implements MessagePlugin {

    /**
     * {@inheritDoc}
     */
    @Override
    public String name() {
        return "wechat";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String process(String payload) {
        return "[WECHAT] 已推送: " + payload;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String discovery() {
        return "spring-bean";
    }
}
