package com.xiesn.springboot.web.exception.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 创建订单入参（用于校验失败演示）。
 */
public class OrderCreateRequest {

    /** 商品编码 */
    @NotBlank(message = "不能为空")
    private String sku;

    /** 购买数量 */
    @NotNull(message = "不能为空")
    @Min(value = 1, message = "至少为 1")
    private Integer quantity;

    /**
     * 获取 sku。
     *
     * @return sku
     */
    public String getSku() {
        return sku;
    }

    /**
     * 设置 sku。
     *
     * @param sku sku
     */
    public void setSku(String sku) {
        this.sku = sku;
    }

    /**
     * 获取数量。
     *
     * @return 数量
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * 设置数量。
     *
     * @param quantity 数量
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
