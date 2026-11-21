package com.xiesn.springboot.jasypt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 演示用敏感配置绑定。
 * <p>{@code db-password} 在 yaml 里以 {@code ENC(...)} 存储，注入到本对象时已是明文。</p>
 */
@ConfigurationProperties(prefix = "demo.secret")
public class DemoSecretProperties {

    /** 对照用明文配置（仅演示，真实项目勿这样放密钥） */
    private String apiKey;

    /** 数据库密码：配置文件中为 ENC 密文，运行时自动解密 */
    private String dbPassword;

    /**
     * 获取对照用 API Key。
     *
     * @return API Key 明文
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * 设置对照用 API Key。
     *
     * @param apiKey API Key
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * 获取已解密的数据库密码。
     *
     * @return 数据库密码明文
     */
    public String getDbPassword() {
        return dbPassword;
    }

    /**
     * 设置数据库密码（框架注入时传入解密后的值）。
     *
     * @param dbPassword 数据库密码
     */
    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }
}
