package com.xiesn.springboot.i18n.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * 用 Spring {@link MessageSource} 把 key 译成句子。前后端分离时不要走这条，留给服务端模板或对照接口。
 */
@Service
public class I18nMessageService {

    private final MessageSource messageSource;

    /**
     * @param messageSource Boot 按 {@code spring.messages.basename} 装好的文案源
     */
    public I18nMessageService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * 解析文案。资源里没有该 key 时退回 key，避免把 {@code NoSuchMessageException} 抛给调用方。
     *
     * @param key    i18n key
     * @param locale 请求语言
     * @return 译文或 key
     */
    public String resolve(String key, Locale locale) {
        return messageSource.getMessage(key, null, key, locale);
    }
}
