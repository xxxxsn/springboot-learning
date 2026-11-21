package com.xiesn.springboot.web.validation.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 嵌套地址入参。
 */
public class AddressRequest {

    /** 省份 */
    @NotBlank(message = "不能为空")
    private String province;

    /** 城市 */
    @NotBlank(message = "不能为空")
    private String city;

    /** 详细地址 */
    @NotBlank(message = "不能为空")
    private String detail;

    /**
     * 获取省份。
     *
     * @return 省份
     */
    public String getProvince() {
        return province;
    }

    /**
     * 设置省份。
     *
     * @param province 省份
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * 获取城市。
     *
     * @return 城市
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置城市。
     *
     * @param city 城市
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 获取详细地址。
     *
     * @return 详细地址
     */
    public String getDetail() {
        return detail;
    }

    /**
     * 设置详细地址。
     *
     * @param detail 详细地址
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }
}
