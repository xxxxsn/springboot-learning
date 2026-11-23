package com.xiesn.springboot.web.resolver.web;

import com.xiesn.springboot.common.result.R;
import com.xiesn.springboot.web.resolver.annotation.ApiResult;
import com.xiesn.springboot.web.resolver.annotation.CurrentUser;
import com.xiesn.springboot.web.resolver.domain.LoginUser;
import com.xiesn.springboot.web.resolver.domain.UserProfile;
import com.xiesn.springboot.web.resolver.support.LoginUserAttrs;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 对照：参数解析、统一包装、以及不经过包装的原始对象。
 */
@RestController
@RequestMapping("/api/resolver")
public class ResolverDemoController {

    /**
     * 说明本模块的分工，不要求登录。
     *
     * @return 规则
     */
    @GetMapping("/overview")
    public R<Map<String, Object>> overview() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("port", 8028);
        data.put("userIdHeader", LoginUserAttrs.USER_ID_HEADER);
        data.put("userNameHeader", LoginUserAttrs.USER_NAME_HEADER);
        data.put("rules", List.of(
                "Filter 只把 X-User-Id / X-User-Name 放进 request",
                "@CurrentUser 从 request 取出 LoginUser，required 默认 true",
                "@ApiResult 把返回值包成 R，已是 R 则不重复包装",
                "没有 @ApiResult 的方法仍按 @RestController 原样写出"
        ));
        return R.ok(data);
    }

    /**
     * 必须有当前用户，返回值由 {@link ApiResult} 包装。
     *
     * @param user 当前用户
     * @return 资料
     */
    @ApiResult
    @GetMapping("/me")
    public UserProfile me(@CurrentUser LoginUser user) {
        return UserProfile.from(user);
    }

    /**
     * 用户可选。没有请求头时得到访客资料。
     *
     * @param user 当前用户，可为 null
     * @return 资料
     */
    @ApiResult
    @GetMapping("/me/optional")
    public UserProfile optional(@CurrentUser(required = false) LoginUser user) {
        return UserProfile.from(user);
    }

    /**
     * 返回值已经是 {@code R}，处理器不应再包一层。
     *
     * @return 已包装的结果
     */
    @ApiResult
    @GetMapping("/already-wrapped")
    public R<String> alreadyWrapped() {
        return R.ok("kept");
    }

    /**
     * 不标注 {@link ApiResult}，响应体就是资料本身。
     *
     * @param user 当前用户，可为 null
     * @return 原始资料
     */
    @GetMapping("/plain")
    public UserProfile plain(@CurrentUser(required = false) LoginUser user) {
        return UserProfile.from(user);
    }
}
