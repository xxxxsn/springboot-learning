package com.xiesn.springboot.spi.plugin.springbean;

import com.xiesn.springboot.spi.plugin.api.MessagePlugin;
import org.springframework.stereotype.Component;

/**
 * Spring Bean 推送插件：由容器扫描注册，不是 ServiceLoader。
 */
@Component
public class PushMessagePlugin implements MessagePlugin {

    /**
     * {@inheritDoc}
     */
    @Override
    public String name() {
        return "push";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String process(String payload) {
        return "[PUSH] 已推送: " + payload;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String discovery() {
        return "spring-bean";
    }
}
