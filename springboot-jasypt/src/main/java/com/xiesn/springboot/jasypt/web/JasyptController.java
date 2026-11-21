package com.xiesn.springboot.jasypt.web;

import com.xiesn.springboot.common.result.R;
import com.xiesn.springboot.jasypt.config.DemoSecretProperties;
import com.xiesn.springboot.jasypt.service.JasyptCryptoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Jasypt 演示接口。
 */
@RestController
@RequestMapping("/api/jasypt")
public class JasyptController {

    private final DemoSecretProperties demoSecretProperties;
    private final JasyptCryptoService jasyptCryptoService;

    /**
     * 注入配置与加解密服务。
     *
     * @param demoSecretProperties 演示敏感配置
     * @param jasyptCryptoService  加解密服务
     */
    public JasyptController(DemoSecretProperties demoSecretProperties,
                            JasyptCryptoService jasyptCryptoService) {
        this.demoSecretProperties = demoSecretProperties;
        this.jasyptCryptoService = jasyptCryptoService;
    }

    /**
     * 查看配置注入结果：明文项原样返回，密文项展示解密后的值（打码对照）。
     *
     * @return 配置快照
     */
    @GetMapping("/secrets")
    public R<Map<String, Object>> secrets() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("apiKey", demoSecretProperties.getApiKey());
        body.put("dbPasswordDecrypted", demoSecretProperties.getDbPassword());
        body.put("dbPasswordMasked", mask(demoSecretProperties.getDbPassword()));
        body.put("tip", "dbPassword 在 yaml 中是 ENC(...)，此处已是自动解密后的明文");
        return R.ok(body);
    }

    /**
     * 把明文加密成可直接粘贴进配置文件的 ENC 串。
     *
     * @param plainText 明文
     * @return ENC(...) 字符串
     */
    @PostMapping("/encrypt")
    public R<Map<String, String>> encrypt(@RequestParam String plainText) {
        String enc = jasyptCryptoService.encryptToEnc(plainText);
        Map<String, String> body = new LinkedHashMap<>();
        body.put("plainText", plainText);
        body.put("encValue", enc);
        body.put("hint", "复制 encValue 到 application.yml；同一明文多次加密结果不同（随机盐/IV），均可解密");
        return R.ok(body);
    }

    /**
     * 解密 ENC 或裸密文。
     *
     * @param cipherText 密文
     * @return 明文
     */
    @PostMapping("/decrypt")
    public R<Map<String, String>> decrypt(@RequestParam String cipherText) {
        String plain = jasyptCryptoService.decrypt(cipherText);
        Map<String, String> body = new LinkedHashMap<>();
        body.put("cipherText", cipherText);
        body.put("plainText", plain);
        return R.ok(body);
    }

    /**
     * 简单打码，避免日志/响应一眼泄露完整密钥。
     *
     * @param value 原始字符串
     * @return 打码后的字符串
     */
    private String mask(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        if (value.length() <= 4) {
            return "****";
        }
        return value.substring(0, 2) + "****" + value.substring(value.length() - 2);
    }
}
