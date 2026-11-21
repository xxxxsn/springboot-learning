package com.xiesn.springboot.i18n.dto;

/**
 * MessageSource 按当前语言解析后的文案，仅供对照。
 *
 * @param key    i18n key
 * @param locale 实际使用的语言标签
 * @param text   解析出的句子；找不到 key 时退回 key 本身
 */
public record ResolvedMessage(String key, String locale, String text) {
}
