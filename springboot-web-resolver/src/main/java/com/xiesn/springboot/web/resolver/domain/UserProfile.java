package com.xiesn.springboot.web.resolver.domain;

/**
 * 给前端看的当前用户资料。
 *
 * @param id    用户编号；访客没有
 * @param name  显示名
 * @param guest 是否未登录
 */
public record UserProfile(Long id, String name, boolean guest) {

    /**
     * 由登录用户或空值生成资料。
     *
     * @param user 当前用户，可为 null
     * @return 资料
     */
    public static UserProfile from(LoginUser user) {
        if (user == null) {
            return new UserProfile(null, "guest", true);
        }
        return new UserProfile(user.id(), user.name(), false);
    }
}
