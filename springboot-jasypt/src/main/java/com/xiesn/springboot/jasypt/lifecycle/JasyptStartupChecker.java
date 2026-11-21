package com.xiesn.springboot.jasypt.lifecycle;

import com.xiesn.springboot.jasypt.config.DemoSecretProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 启动后自检：确认 ENC 配置已被解密成预期明文。
 */
@Component
public class JasyptStartupChecker implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(JasyptStartupChecker.class);

    /** 与生成 ENC 时使用的明文保持一致，仅用于演示断言 */
    private static final String EXPECTED_DB_PASSWORD = "MySecretDbPassword!";

    private final DemoSecretProperties demoSecretProperties;

    /**
     * 注入演示配置。
     *
     * @param demoSecretProperties 演示敏感配置
     */
    public JasyptStartupChecker(DemoSecretProperties demoSecretProperties) {
        this.demoSecretProperties = demoSecretProperties;
    }

    /**
     * 启动完成后核对解密结果。
     *
     * @param args 启动参数
     */
    @Override
    public void run(ApplicationArguments args) {
        String actual = demoSecretProperties.getDbPassword();
        if (EXPECTED_DB_PASSWORD.equals(actual)) {
            log.info("Jasypt 解密自检通过：demo.secret.db-password 已还原为预期明文");
        } else {
            log.warn("Jasypt 解密自检未通过：请确认 JASYPT_ENCRYPTOR_PASSWORD 是否为 springboot-learning-demo，当前值长度={}",
                    actual == null ? 0 : actual.length());
        }
    }
}
