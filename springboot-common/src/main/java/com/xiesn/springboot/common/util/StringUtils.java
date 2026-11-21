package com.xiesn.springboot.common.util;

/**
 * 轻量字符串工具。
 * <p>百科项目里高频判断集中放这里，避免各模块复制 {@code isBlank} 逻辑。</p>
 */
public final class StringUtils {

    /**
     * 工具类不允许实例化。
     */
    private StringUtils() {
    }

    /**
     * 判断字符串是否为 null 或仅空白。
     *
     * @param text 待检查字符串
     * @return 空白则 true
     */
    public static boolean isBlank(String text) {
        return text == null || text.isBlank();
    }

    /**
     * 判断字符串是否有可见内容。
     *
     * @param text 待检查字符串
     * @return 有内容则 true
     */
    public static boolean hasText(String text) {
        return !isBlank(text);
    }

    /**
     * 空串时返回默认值。
     *
     * @param text         原始值
     * @param defaultValue 默认值
     * @return 非空白原始值或默认值
     */
    public static String defaultIfBlank(String text, String defaultValue) {
        return isBlank(text) ? defaultValue : text;
    }
}
