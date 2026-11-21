package com.xiesn.springboot.profiles.feature;

/**
 * 环境横幅：由 {@code @Profile} 决定哪个实现进容器。
 */
public interface EnvBanner {

    /**
     * 横幅文本。
     *
     * @return 文案
     */
    String banner();

    /**
     * 绑定的 profile 名。
     *
     * @return profile
     */
    String profile();
}