package com.xiesn.springboot.web.validation.support;

import com.xiesn.springboot.common.exception.BusinessException;
import com.xiesn.springboot.common.result.CommonErrorCode;
import com.xiesn.springboot.web.validation.dto.FieldErrorItem;
import com.xiesn.springboot.web.validation.dto.UserSaveRequest;
import com.xiesn.springboot.web.validation.dto.UserView;
import com.xiesn.springboot.web.validation.validation.groups.Create;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 手动 Validator 演示：模拟 MQ / Job 等非 HTTP 入口。
 */
@Component
public class ManualValidationDemo {

    private final Validator validator;
    private final AtomicLong idSeq = new AtomicLong(9000);

    /**
     * 注入标准 Validator。
     *
     * @param validator Bean Validation 实现
     */
    public ManualValidationDemo(Validator validator) {
        this.validator = validator;
    }

    /**
     * 按 Create 分组手动校验，通过后生成回显。
     *
     * @param request 入参
     * @return 回显
     */
    public UserView processLikeMq(UserSaveRequest request) {
        Set<ConstraintViolation<UserSaveRequest>> violations = validator.validate(request, Create.class);
        if (!violations.isEmpty()) {
            List<FieldErrorItem> errors = violations.stream()
                    .map(v -> new FieldErrorItem(v.getPropertyPath().toString(), v.getMessage()))
                    .toList();
            throw new ManualValidationException(errors);
        }
        long id = idSeq.incrementAndGet();
        String city = request.getAddress() == null ? null : request.getAddress().getCity();
        return new UserView(id, request.getUsername(), request.getMobile(), request.getStatus(), city);
    }

    /**
     * 手动校验失败：携带字段错误列表，由 Advice 转成 {@code R}。
     */
    public static class ManualValidationException extends BusinessException {

        private final List<FieldErrorItem> errors;

        /**
         * 构造。
         *
         * @param errors 字段错误
         */
        public ManualValidationException(List<FieldErrorItem> errors) {
            super(CommonErrorCode.BAD_REQUEST, "参数校验失败");
            this.errors = errors;
        }

        /**
         * 获取字段错误列表。
         *
         * @return 错误列表
         */
        public List<FieldErrorItem> getErrors() {
            return errors;
        }
    }
}
