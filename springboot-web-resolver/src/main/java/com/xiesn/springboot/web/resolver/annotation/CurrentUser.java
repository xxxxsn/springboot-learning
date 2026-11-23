package com.xiesn.springboot.web.resolver.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 把当前登录用户注入控制器参数。
 * <p>只标记参数，不负责鉴权。用户对象由 Filter 提前放进 request。</p>
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUser {

    /**
     * 为 true 时没有用户就抛未认证异常；为 false 时注入 null。
     *
     * @return 是否必须有登录用户
     */
    boolean required() default true;
}
