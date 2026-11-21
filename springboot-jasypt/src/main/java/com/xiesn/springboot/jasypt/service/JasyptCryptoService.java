package com.xiesn.springboot.jasypt.service;

import com.xiesn.springboot.common.util.AssertUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 加解密演示服务。
 * <p>复用 starter 自动创建的懒加载 {@link StringEncryptor}。
 * 在 jasypt-spring-boot 4.0.4 中 Bean 名为 {@code lazyJasyptStringEncryptor}
 *（自定义加密器才叫 {@code jasyptStringEncryptor}）。</p>
 */
@Service
public class JasyptCryptoService {

    private final StringEncryptor stringEncryptor;

    /**
     * 注入框架提供的字符串加解密器。
     *
     * @param stringEncryptor Jasypt 字符串加解密器
     */
    public JasyptCryptoService(@Qualifier("lazyJasyptStringEncryptor") StringEncryptor stringEncryptor) {
        this.stringEncryptor = stringEncryptor;
    }

    /**
     * 加密明文，并包装成配置文件可用的 {@code ENC(...)} 形式。
     *
     * @param plainText 明文
     * @return ENC 包装后的密文
     */
    public String encryptToEnc(String plainText) {
        AssertUtils.hasText(plainText, "明文不能为空");
        return "ENC(" + stringEncryptor.encrypt(plainText) + ")";
    }

    /**
     * 解密密文。
     * <p>既支持裸密文，也支持带 {@code ENC(...)} 前缀的完整写法。</p>
     *
     * @param cipherText 密文或 ENC(...) 字符串
     * @return 明文
     */
    public String decrypt(String cipherText) {
        AssertUtils.hasText(cipherText, "密文不能为空");
        String raw = unwrapEnc(cipherText);
        return stringEncryptor.decrypt(raw);
    }

    /**
     * 去掉 {@code ENC(...)} 外壳，得到算法真正需要的密文。
     *
     * @param value 可能带 ENC 前缀的值
     * @return 裸密文
     */
    private String unwrapEnc(String value) {
        String trimmed = value.trim();
        if (trimmed.startsWith("ENC(") && trimmed.endsWith(")")) {
            return trimmed.substring(4, trimmed.length() - 1);
        }
        return trimmed;
    }
}
