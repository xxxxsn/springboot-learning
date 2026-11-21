package com.xiesn.springboot.web.interceptor.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 拦截器演示配置。
 */
@ConfigurationProperties(prefix = "demo.interceptor")
public class InterceptorDemoProperties {

    /** 演示 Token，仅教学 */
    private String demoToken = "demo-token-8027";

    /**
     * 获取演示 Token。
     *
     * @return token
     */
    public String getDemoToken() {
        return demoToken;
    }

    /**
     * 设置演示 Token。
     *
     * @param demoToken token
     */
    public void setDemoToken(String demoToken) {
        this.demoToken = demoToken;
    }
}
