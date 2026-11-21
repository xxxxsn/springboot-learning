package com.xiesn.springboot.maven.multimodule.api;

import com.xiesn.springboot.maven.multimodule.api.dto.ProductDto;

import java.util.List;

/**
 * 商品业务门面。
 * <p>接口定义在 api 模块，实现在 service 模块，web 只依赖接口或实现 jar，体现分层解耦。</p>
 */
public interface ProductFacade {

    /**
     * 按编号查询商品。
     *
     * @param id 商品编号
     * @return 商品信息；不存在时由实现抛业务异常
     */
    ProductDto getById(Long id);

    /**
     * 列出全部演示商品。
     *
     * @return 商品列表
     */
    List<ProductDto> listAll();
}
