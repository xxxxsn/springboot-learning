package com.xiesn.springboot.web.validation.dto;

import com.xiesn.springboot.web.validation.domain.UserStatus;
import com.xiesn.springboot.web.validation.validation.EnumValue;
import com.xiesn.springboot.web.validation.validation.Mobile;
import com.xiesn.springboot.web.validation.validation.groups.Create;
import com.xiesn.springboot.web.validation.validation.groups.Update;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

/**
 * 用户保存入参：同一 DTO + 分组区分创建/更新。
 */
public class UserSaveRequest {

    /** 更新时必填；创建时必须为空 */
    @Null(groups = Create.class, message = "创建时不能传 id")
    @NotNull(groups = Update.class, message = "更新时 id 不能为空")
    private Long id;

    /** 用户名 */
    @NotBlank(groups = {Create.class, Update.class}, message = "不能为空")
    @Size(min = 2, max = 32, groups = {Create.class, Update.class}, message = "长度须在 2~32")
    private String username;

    /** 手机号：创建必填；更新可选但格式仍校验 */
    @NotBlank(groups = Create.class, message = "不能为空")
    @Mobile(groups = {Create.class, Update.class})
    private String mobile;

    /** 状态：ENABLED / DISABLED */
    @NotBlank(groups = {Create.class, Update.class}, message = "不能为空")
    @EnumValue(value = UserStatus.class, groups = {Create.class, Update.class})
    private String status;

    /** 嵌套地址 */
    @NotNull(groups = {Create.class, Update.class}, message = "不能为空")
    @Valid
    private AddressRequest address;

    /**
     * 获取 id。
     *
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置 id。
     *
     * @param id id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取用户名。
     *
     * @return 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名。
     *
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取手机号。
     *
     * @return 手机号
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置手机号。
     *
     * @param mobile 手机号
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取状态。
     *
     * @return 状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态。
     *
     * @param status 状态
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取地址。
     *
     * @return 地址
     */
    public AddressRequest getAddress() {
        return address;
    }

    /**
     * 设置地址。
     *
     * @param address 地址
     */
    public void setAddress(AddressRequest address) {
        this.address = address;
    }
}
