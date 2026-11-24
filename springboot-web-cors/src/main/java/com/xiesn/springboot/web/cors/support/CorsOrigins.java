package com.xiesn.springboot.web.cors.support;

/**
 * 演示用源站。凭证场景必须写具体源，不能用 {@code *}。
 */
public final class CorsOrigins {

    /** 全局 {@code /api/cors/**} 允许的管理端。 */
    public static final String ADMIN = "https://admin.example.com";

    /** 只在 {@code /shop} 上通过 {@code @CrossOrigin} 追加的商城。 */
    public static final String SHOP = "https://shop.example.com";

    /** 白名单之外，应被拒绝。 */
    public static final String EVIL = "https://evil.example.com";

    /** 允许前端脚本读取的响应头。 */
    public static final String EXPOSED_HEADER = "X-Demo-Token";

    /** 预检缓存秒数。 */
    public static final long MAX_AGE_SECONDS = 1800L;

    private CorsOrigins() {
    }
}
