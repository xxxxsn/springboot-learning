package com.xiesn.springboot.profiles.web;

import com.xiesn.springboot.common.result.R;
import com.xiesn.springboot.profiles.service.ProfilesDemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Profile 观测 HTTP 入口。
 */
@RestController
@RequestMapping("/api/profiles")
public class ProfilesController {

    private final ProfilesDemoService profilesDemoService;

    /**
     * 注入演示服务。
     *
     * @param profilesDemoService 服务
     */
    public ProfilesController(ProfilesDemoService profilesDemoService) {
        this.profilesDemoService = profilesDemoService;
    }

    /**
     * 总览。
     *
     * @return 汇总
     */
    @GetMapping("/overview")
    public R<Map<String, Object>> overview() {
        return R.ok(profilesDemoService.overview());
    }

    /**
     * 生效 demo.*。
     *
     * @return 映射
     */
    @GetMapping("/effective")
    public R<Map<String, Object>> effective() {
        return R.ok(profilesDemoService.effectiveDemo());
    }

    /**
     * 当前 Banner。
     *
     * @return 结果
     */
    @GetMapping("/banner")
    public R<Map<String, Object>> banner() {
        return R.ok(profilesDemoService.banner());
    }
}