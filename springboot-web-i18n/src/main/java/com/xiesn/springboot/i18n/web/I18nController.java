package com.xiesn.springboot.i18n.web;

import com.xiesn.springboot.common.result.R;
import com.xiesn.springboot.i18n.dto.FileCheckRequest;
import com.xiesn.springboot.i18n.dto.LoginRequest;
import com.xiesn.springboot.i18n.dto.ResolvedMessage;
import com.xiesn.springboot.i18n.service.I18nDemoService;
import com.xiesn.springboot.i18n.service.I18nMessageService;
import jakarta.validation.Valid;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

/**
 * 国际化演示接口。业务失败的 {@code message} 是 key；{@code /messages} 才按语言返回句子。
 */
@RestController
@RequestMapping("/api/i18n")
public class I18nController {

    private final I18nDemoService i18nDemoService;
    private final I18nMessageService i18nMessageService;

    /**
     * @param i18nDemoService    登录与文件类型演示
     * @param i18nMessageService MessageSource 对照
     */
    public I18nController(I18nDemoService i18nDemoService, I18nMessageService i18nMessageService) {
        this.i18nDemoService = i18nDemoService;
        this.i18nMessageService = i18nMessageService;
    }

    /**
     * 登录。密码不是 {@code secret} 时返回 {@code auth.login.invalid_credentials}。
     *
     * @param request 用户名与密码
     * @return 成功时带回用户名
     */
    @PostMapping("/login")
    public R<String> login(@Valid @RequestBody LoginRequest request) {
        i18nDemoService.login(request.username(), request.password());
        return R.ok(request.username());
    }

    /**
     * 检查扩展名。不允许时返回 {@code file.type_not_allowed}。
     *
     * @param request 文件名
     * @return 成功时带回原文件名
     */
    @PostMapping("/files")
    public R<String> checkFile(@Valid @RequestBody FileCheckRequest request) {
        i18nDemoService.checkFile(request.filename());
        return R.ok(request.filename());
    }

    /**
     * 按 {@code Accept-Language} 把 key 译成句子。这是服务端翻译对照，分离前端不应依赖它。
     *
     * @param key i18n key
     * @return 语言标签与译文
     */
    @GetMapping("/messages")
    public R<ResolvedMessage> message(@RequestParam String key) {
        Locale locale = LocaleContextHolder.getLocale();
        String text = i18nMessageService.resolve(key, locale);
        return R.ok(new ResolvedMessage(key, locale.toLanguageTag(), text));
    }
}
