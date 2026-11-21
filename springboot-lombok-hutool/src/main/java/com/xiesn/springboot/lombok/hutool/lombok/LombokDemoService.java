package com.xiesn.springboot.lombok.hutool.lombok;

import com.xiesn.springboot.lombok.hutool.lombok.model.BuilderUser;
import com.xiesn.springboot.lombok.hutool.lombok.model.DataEmployee;
import com.xiesn.springboot.lombok.hutool.lombok.model.SafeEmployee;
import com.xiesn.springboot.lombok.hutool.lombok.model.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Lombok 常用注解与坑点演示。
 */
@Slf4j
@Service
public class LombokDemoService {

    /**
     * 汇总常用注解用法。
     *
     * @return 演示结果
     */
    public Map<String, Object> overview() {
        UserDto built = UserDto.builder()
                .id(1L)
                .name("张三")
                .email("zhangsan@example.com")
                .build();
        log.info("Slf4j 由 @Slf4j 生成，user={}", built);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("case", "常用注解：Getter/Setter/ToString/EqualsAndHashCode/Builder/构造器");
        result.put("input", Map.of("id", 1, "name", "张三"));
        result.put("output", Map.of(
                "user", built.toString(),
                "equalsSelf", built.equals(UserDto.builder().id(1L).name("张三").email("zhangsan@example.com").build()),
                "logHint", "@Slf4j 生成 private static final Logger log"));
        result.put("pitfall", "DTO 优先拆开注解；Entity 慎用 @Data（会生成 equals/hashCode/toString 全家桶）");
        return result;
    }

    /**
     * 演示已修好的 Builder + Jackson 反序列化路径。
     *
     * @param user 请求体绑定到 BuilderUser
     * @return 结果与坑点说明
     */
    public Map<String, Object> jackson(BuilderUser user) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("case", "Builder + Jackson：无参/全参齐全才能反序列化");
        result.put("input", user);
        result.put("output", Map.of(
                "username", user.getUsername(),
                "age", user.getAge(),
                "rebuild", BuilderUser.builder().username(user.getUsername()).age(user.getAge()).build().toString()));
        result.put("pitfall",
                "只写 @Builder 时默认无私有无参构造，Jackson 报错 InvalidDefinitionException；"
                        + "需 @NoArgsConstructor + @AllArgsConstructor，或 @Jacksonized @Builder");
        return result;
    }

    /**
     * 对比 @Data 全字段 equals 与 onlyExplicitlyIncluded。
     *
     * @return 对比结果
     */
    public Map<String, Object> equalsDemo() {
        DataEmployee a = new DataEmployee();
        a.setEmpNo("E001");
        a.setName("李四");
        a.setRoles(new ArrayList<>(List.of("DEV")));

        DataEmployee b = new DataEmployee();
        b.setEmpNo("E001");
        b.setName("李四");
        b.setRoles(new ArrayList<>(List.of("DEV")));

        boolean dataEqualBefore = a.equals(b);
        b.getRoles().add("LEAD");
        boolean dataEqualAfterMutate = a.equals(b);

        SafeEmployee s1 = new SafeEmployee();
        s1.setEmpNo("E001");
        s1.setName("李四");
        s1.setRoles(new ArrayList<>(List.of("DEV")));

        SafeEmployee s2 = new SafeEmployee();
        s2.setEmpNo("E001");
        s2.setName("李四");
        s2.setRoles(new ArrayList<>(List.of("DEV", "LEAD")));

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("case", "equals/hashCode：可变集合是否参与比较");
        result.put("input", Map.of("empNo", "E001", "rolesMutated", true));
        result.put("output", Map.of(
                "dataEmployeeEqualBeforeMutate", dataEqualBefore,
                "dataEmployeeEqualAfterMutate", dataEqualAfterMutate,
                "safeEmployeeEqualDespiteRoleDiff", s1.equals(s2)));
        result.put("pitfall",
                "@Data 默认把 roles 纳入 equals；集合变更后 equals 变 false，放进 HashSet 会丢元素。"
                        + "业务主键用 @EqualsAndHashCode.Include + onlyExplicitlyIncluded=true");
        return result;
    }
}
