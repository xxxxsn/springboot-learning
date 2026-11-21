package com.xiesn.springboot.web.validation.web;

import com.xiesn.springboot.common.result.R;
import com.xiesn.springboot.web.validation.dto.UserSaveRequest;
import com.xiesn.springboot.web.validation.dto.UserView;
import com.xiesn.springboot.web.validation.service.UserValidationService;
import com.xiesn.springboot.web.validation.support.ManualValidationDemo;
import com.xiesn.springboot.web.validation.validation.Mobile;
import com.xiesn.springboot.web.validation.validation.groups.Create;
import com.xiesn.springboot.web.validation.validation.groups.Update;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用户校验演示接口。
 */
@RestController
@RequestMapping("/api/validation")
@Validated
public class UserValidationController {

    private final UserValidationService userValidationService;
    private final ManualValidationDemo manualValidationDemo;
    private final Validator validator;

    /**
     * 注入服务与 Validator。
     *
     * @param userValidationService 用户服务
     * @param manualValidationDemo  手动校验演示
     * @param validator             Bean Validation
     */
    public UserValidationController(UserValidationService userValidationService,
                                    ManualValidationDemo manualValidationDemo,
                                    Validator validator) {
        this.userValidationService = userValidationService;
        this.manualValidationDemo = manualValidationDemo;
        this.validator = validator;
    }

    /**
     * 能力与约定总览。
     *
     * @return 说明
     */
    @GetMapping("/overview")
    public R<Map<String, Object>> overview() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("module", "springboot-web-validation");
        data.put("port", 8024);
        data.put("capabilities", List.of(
                "自定义 @Mobile / @EnumValue",
                "Create/Update 分组",
                "嵌套 @Valid",
                "方法级参数校验",
                "手动 Validator（模拟 MQ）",
                "校验错误列表进 R.data"
        ));
        data.put("boundary", Map.of(
                "problemDetail", "#25 springboot-web-exception",
                "jackson", "#26 springboot-web-jackson",
                "i18n", "#31 springboot-web-i18n"
        ));
        data.put("tip", "实战可将 validation 包装下沉到公司 common/framework");
        return R.ok(data);
    }

    /**
     * 创建：按 Create 分组校验。
     *
     * @param request 入参
     * @return 回显
     */
    @PostMapping("/users")
    public R<UserView> create(@Validated(Create.class) @RequestBody UserSaveRequest request) {
        return R.ok(userValidationService.create(request));
    }

    /**
     * 更新：先写入路径 id，再按 Update 分组校验（避免注解校验早于 setId）。
     *
     * @param id      路径 id
     * @param request 入参
     * @return 回显
     */
    @PutMapping("/users/{id}")
    public R<UserView> update(@PathVariable Long id, @RequestBody UserSaveRequest request) {
        request.setId(id);
        Set<ConstraintViolation<UserSaveRequest>> violations = validator.validate(request, Update.class);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return R.ok(userValidationService.update(request));
    }

    /**
     * 嵌套失败演示入口：与 create 相同，便于 api.http 对照坏地址。
     *
     * @param request 入参
     * @return 回显
     */
    @PostMapping("/users/nested-fail")
    public R<UserView> nestedFail(@Validated(Create.class) @RequestBody UserSaveRequest request) {
        return R.ok(userValidationService.create(request));
    }

    /**
     * 方法级校验：Controller 参数上的 {@link Mobile}（类上需 {@code @Validated}）。
     *
     * @param mobile 手机号
     * @return 规范化结果
     */
    @GetMapping("/users/check-mobile")
    public R<Map<String, String>> checkMobile(@RequestParam @Mobile String mobile) {
        return R.ok(Map.of("mobile", userValidationService.checkMobile(mobile)));
    }

    /**
     * 手动 Validator：模拟非 HTTP 入口。
     *
     * @param request 入参
     * @return 回显
     */
    @PostMapping("/manual")
    public R<UserView> manual(@RequestBody UserSaveRequest request) {
        return R.ok(manualValidationDemo.processLikeMq(request));
    }
}
