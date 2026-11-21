package com.xiesn.springboot.jasypt;

import com.xiesn.springboot.jasypt.service.JasyptCryptoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 加解密往返测试。
 */
@SpringBootTest
class JasyptCryptoServiceTest {

    @Autowired
    private JasyptCryptoService jasyptCryptoService;

    /**
     * 验证加密后再解密能还原明文。
     */
    @Test
    void encryptThenDecrypt_shouldRestorePlainText() {
        String plain = "RoundTrip-Secret-001";
        String enc = jasyptCryptoService.encryptToEnc(plain);
        assertTrue(enc.startsWith("ENC("));
        assertEquals(plain, jasyptCryptoService.decrypt(enc));
    }

    /**
     * 验证配置文件中预置的 ENC 样例可被正确解密。
     */
    @Test
    void decryptSampleEnc_shouldMatchExpectedPassword() {
        String sample = "ENC(cK6P3/nvsc8Z2NHTHriH4w0Mt6fdde71OyDeEHnk4xVJcaSRY4AaSovxHTE0Pq283bLp+SdecPKAb9/8nPigfQ==)";
        assertEquals("MySecretDbPassword!", jasyptCryptoService.decrypt(sample));
    }
}
