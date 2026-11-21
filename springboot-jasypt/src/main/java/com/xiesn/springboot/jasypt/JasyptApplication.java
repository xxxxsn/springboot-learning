package com.xiesn.springboot.jasypt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Jasypt 配置加密演示启动类。
 * <p>引入 {@code jasypt-spring-boot-starter} 后，Environment 中的 {@code ENC(...)} 会在读取时自动解密。</p>
 */
@SpringBootApplication
public class JasyptApplication {

    /**
     * 应用入口。
     * <p>主密钥请用环境变量 {@code JASYPT_ENCRYPTOR_PASSWORD} 或
     * {@code -Djasypt.encryptor.password=} 传入，不要提交到仓库。</p>
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(JasyptApplication.class, args);
    }
}
