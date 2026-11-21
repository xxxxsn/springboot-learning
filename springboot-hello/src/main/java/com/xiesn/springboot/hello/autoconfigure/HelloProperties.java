package com.xiesn.springboot.hello.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Hello 自动配置绑定属性。
 * <p>对应配置前缀 {@code demo.hello}，展示 Boot 3 中 {@code @ConfigurationProperties} 的典型用法。</p>
 */
@ConfigurationProperties(prefix = "demo.hello")
public class HelloProperties {

    /** 是否启用 Hello 能力，默认开启 */
    private boolean enabled = true;

    /** 问候语前缀 */
    private String greeting = "你好";

    /** 默认称呼 */
    private String defaultName = "Spring Boot 3";

    /**
     * 是否启用。
     *
     * @return true 表示启用
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * 设置是否启用。
     *
     * @param enabled 启用开关
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * 获取问候前缀。
     *
     * @return 问候前缀
     */
    public String getGreeting() {
        return greeting;
    }

    /**
     * 设置问候前缀。
     *
     * @param greeting 问候前缀
     */
    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

    /**
     * 获取默认称呼。
     *
     * @return 默认称呼
     */
    public String getDefaultName() {
        return defaultName;
    }

    /**
     * 设置默认称呼。
     *
     * @param defaultName 默认称呼
     */
    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }
}
