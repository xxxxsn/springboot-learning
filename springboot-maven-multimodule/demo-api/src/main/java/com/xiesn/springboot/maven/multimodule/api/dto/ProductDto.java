package com.xiesn.springboot.maven.multimodule.api.dto;

import java.math.BigDecimal;

/**
 * 商品传输对象。
 * <p>放在 api 模块，供 web、其它客户端或测试共用，避免 service 实现细节泄漏。</p>
 */
public class ProductDto {

    /** 商品编号 */
    private Long id;

    /** 商品名称 */
    private String name;

    /** 单价 */
    private BigDecimal price;

    /**
     * 无参构造，供反序列化使用。
     */
    public ProductDto() {
    }

    /**
     * 全字段构造。
     *
     * @param id    商品编号
     * @param name  名称
     * @param price 单价
     */
    public ProductDto(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    /**
     * 获取商品编号。
     *
     * @return 商品编号
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置商品编号。
     *
     * @param id 商品编号
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取商品名称。
     *
     * @return 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置商品名称。
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取单价。
     *
     * @return 单价
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 设置单价。
     *
     * @param price 单价
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
