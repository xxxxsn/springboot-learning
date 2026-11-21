package com.xiesn.springboot.profiles.feature;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * 仅在 {@code test} profile 下注册。
 */
@Component
@Profile("test")
public class TestEnvBanner implements EnvBanner {

    /**
     * {@inheritDoc}
     */
    @Override
    public String banner() {
        return "[TEST] 自动化测试环境";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String profile() {
        return "test";
    }
}