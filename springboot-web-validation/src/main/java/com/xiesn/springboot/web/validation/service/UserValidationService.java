package com.xiesn.springboot.web.validation.service;

import com.xiesn.springboot.web.validation.dto.UserSaveRequest;
import com.xiesn.springboot.web.validation.dto.UserView;
import com.xiesn.springboot.web.validation.validation.Mobile;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 用户校验演示服务：类上 {@code @Validated} 才能启用方法级参数约束。
 */
@Service
@Validated
public class UserValidationService {

    private final AtomicLong idSeq = new AtomicLong(1000);

    /**
     * 创建用户（入参已在 Controller 按 Create 分组校验过）。
     *
     * @param request 创建入参
     * @return 回显
     */
    public UserView create(UserSaveRequest request) {
        long id = idSeq.incrementAndGet();
        return toView(id, request);
    }

    /**
     * 更新用户（入参已在 Controller 按 Update 分组校验过）。
     *
     * @param request 更新入参
     * @return 回显
     */
    public UserView update(UserSaveRequest request) {
        return toView(request.getId(), request);
    }

    /**
     * 方法级校验样例：直接约束方法参数。
     *
     * @param mobile 手机号
     * @return 规范化后的手机号
     */
    public String checkMobile(@Mobile String mobile) {
        return mobile == null ? null : mobile.trim();
    }

    private UserView toView(Long id, UserSaveRequest request) {
        String city = request.getAddress() == null ? null : request.getAddress().getCity();
        return new UserView(id, request.getUsername(), request.getMobile(), request.getStatus(), city);
    }
}
