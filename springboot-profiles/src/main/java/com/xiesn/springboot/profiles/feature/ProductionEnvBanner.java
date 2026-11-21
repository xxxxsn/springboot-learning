package com.xiesn.springboot.profiles.feature;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * 仅在 {@code production} profile 下注册（含 group 激活）。
 */
@Component
@Profile("production")
public class ProductionEnvBanner implements EnvBanner {

    /**
     * {@inheritDoc}
     */
    @Override
    public String banner() {
        return "[PRODUCTION] 生产加固环境";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String profile() {
        return "production";
    }
}