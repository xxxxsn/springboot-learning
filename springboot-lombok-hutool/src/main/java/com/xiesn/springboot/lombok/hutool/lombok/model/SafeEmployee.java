package com.xiesn.springboot.lombok.hutool.lombok.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 安全 equals 的员工模型：只把业务主键纳入 equals/hashCode。
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SafeEmployee {

    /**
     * 工号（唯一参与 equals/hashCode）。
     */
    @EqualsAndHashCode.Include
    private String empNo;

    /**
     * 姓名。
     */
    private String name;

    /**
     * 角色列表（不参与 equals）。
     */
    private List<String> roles = new ArrayList<>();
}
