package com.xiesn.springboot.lombok.hutool.lombok.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用 {@code @Data} 的员工模型（演示 equals 坑）。
 * <p>{@code @Data} 会对所有字段生成 equals/hashCode，可变集合参与比较时，
 * 集合内容变更会导致 equals 结果变化，放进 HashSet/HashMap 很危险。</p>
 */
@Data
public class DataEmployee {

    /**
     * 工号（业务主键）。
     */
    private String empNo;

    /**
     * 姓名。
     */
    private String name;

    /**
     * 角色列表（可变集合）。
     */
    private List<String> roles = new ArrayList<>();
}
