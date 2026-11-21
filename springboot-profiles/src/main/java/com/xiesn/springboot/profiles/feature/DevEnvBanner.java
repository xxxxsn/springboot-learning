package com.xiesn.springboot.profiles.feature;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * 仅在 {@code dev} profile 下注册。
 */
@Component
@Profile("dev")
public class DevEnvBanner implements EnvBanner {

    /**
     * {@inheritDoc}
     */
    @Override
    public String banner() {
        return "[DEV] 调试友好环境";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String profile() {
        return "dev";
    }
}