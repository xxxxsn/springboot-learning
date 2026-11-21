package com.xiesn.springboot.profiles.service;

import com.xiesn.springboot.profiles.config.DemoAppProperties;
import com.xiesn.springboot.profiles.feature.EnvBanner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Profile / 生效配置观测服务。
 */
@Service
public class ProfilesDemoService {

    private final Environment environment;
    private final DemoAppProperties demoAppProperties;
    private final List<EnvBanner> envBanners;

    /**
     * 注入环境、配置与条件 Bean。
     *
     * @param environment        环境
     * @param demoAppProperties  演示配置
     * @param envBanners         当前装载的 Banner（无匹配 profile 时为空列表）
     */
    public ProfilesDemoService(Environment environment,
                               DemoAppProperties demoAppProperties,
                               List<EnvBanner> envBanners) {
        this.environment = environment;
        this.demoAppProperties = demoAppProperties;
        this.envBanners = envBanners;
    }

    /**
     * 总览：active profiles + 生效配置 + Banner。
     *
     * @return 汇总
     */
    public Map<String, Object> overview() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("case", "多环境观测台");
        result.put("activeProfiles", Arrays.asList(environment.getActiveProfiles()));
        result.put("defaultProfiles", Arrays.asList(environment.getDefaultProfiles()));
        result.put("effective", effectiveDemo());
        result.put("banners", bannerList());
        result.put("pitfall", "命令行/环境变量 > import 文件 > profile 专用 yml > 默认 application.yml；"
                + "group 只能定义在非 profile 专用文档；Compose profile ≠ Spring profile");
        return result;
    }

    /**
     * 仅返回 demo.* 生效值。
     *
     * @return 映射
     */
    public Map<String, Object> effectiveDemo() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("envName", demoAppProperties.getEnvName());
        map.put("message", demoAppProperties.getMessage());
        map.put("featureDebug", demoAppProperties.getFeature().isDebug());
        map.put("datasourceLabel", demoAppProperties.getDatasourceLabel());
        map.put("importHint", demoAppProperties.getImportHint());
        map.put("multidocFlag", demoAppProperties.getMultidocFlag());
        return map;
    }

    /**
     * 当前 @Profile Banner。
     *
     * @return 结果
     */
    public Map<String, Object> banner() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("case", "@Profile 条件 Bean");
        result.put("input", Arrays.asList(environment.getActiveProfiles()));
        result.put("output", bannerList());
        result.put("pitfall", "无匹配 profile 时 List<EnvBanner> 为空，不要假设一定有实现");
        return result;
    }

    private List<Map<String, String>> bannerList() {
        return envBanners.stream()
                .map(b -> {
                    Map<String, String> item = new LinkedHashMap<>();
                    item.put("profile", b.profile());
                    item.put("banner", b.banner());
                    item.put("type", b.getClass().getSimpleName());
                    return item;
                })
                .collect(Collectors.toList());
    }
}