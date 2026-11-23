package com.xiesn.springboot.web.resolver.support;

/**
 * 本模块放在 request 上的属性名。
 */
public final class LoginUserAttrs {

    /** Filter 写入、{@code @CurrentUser} 读取的登录用户。 */
    public static final String LOGIN_USER = "demo.resolver.loginUser";

    /** 用户编号请求头。 */
    public static final String USER_ID_HEADER = "X-User-Id";

    /** 用户显示名请求头。 */
    public static final String USER_NAME_HEADER = "X-User-Name";

    private LoginUserAttrs() {
    }
}
