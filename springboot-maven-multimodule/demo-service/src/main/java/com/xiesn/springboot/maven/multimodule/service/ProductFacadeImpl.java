package com.xiesn.springboot.maven.multimodule.service;

import com.xiesn.springboot.common.exception.BusinessException;
import com.xiesn.springboot.common.result.CommonErrorCode;
import com.xiesn.springboot.common.util.AssertUtils;
import com.xiesn.springboot.maven.multimodule.api.ProductFacade;
import com.xiesn.springboot.maven.multimodule.api.dto.ProductDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品门面实现。
 * <p>使用内存数据演示分层，真实项目这里会调用 Mapper / 远程服务。</p>
 */
@Service
public class ProductFacadeImpl implements ProductFacade {

    /** 演示用内存商品表 */
    private final Map<Long, ProductDto> store = new LinkedHashMap<>();

    /**
     * 初始化几条演示数据。
     */
    public ProductFacadeImpl() {
        store.put(1L, new ProductDto(1L, "Spring Boot 实战", new BigDecimal("89.00")));
        store.put(2L, new ProductDto(2L, "Maven 多模块手册", new BigDecimal("59.90")));
        store.put(3L, new ProductDto(3L, "Java 21 新特性", new BigDecimal("72.50")));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductDto getById(Long id) {
        AssertUtils.notNull(id, "商品编号不能为空");
        ProductDto product = store.get(id);
        if (product == null) {
            throw new BusinessException(CommonErrorCode.NOT_FOUND, "商品不存在: id=" + id);
        }
        return product;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProductDto> listAll() {
        return new ArrayList<>(store.values());
    }
}
