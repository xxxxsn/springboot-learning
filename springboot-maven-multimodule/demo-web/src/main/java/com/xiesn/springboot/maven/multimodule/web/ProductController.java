package com.xiesn.springboot.maven.multimodule.web;

import com.xiesn.springboot.common.result.R;
import com.xiesn.springboot.maven.multimodule.api.ProductFacade;
import com.xiesn.springboot.maven.multimodule.api.dto.ProductDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品 HTTP 接口。
 * <p>web 模块只编排 HTTP，业务全部委托给 api 门面。</p>
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductFacade productFacade;

    /**
     * 注入商品门面（由 service 模块提供实现）。
     *
     * @param productFacade 商品门面
     */
    public ProductController(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    /**
     * 查询商品列表。
     *
     * @return 统一响应
     */
    @GetMapping
    public R<List<ProductDto>> list() {
        return R.ok(productFacade.listAll());
    }

    /**
     * 按编号查询商品。
     *
     * @param id 商品编号
     * @return 统一响应
     */
    @GetMapping("/{id}")
    public R<ProductDto> detail(@PathVariable Long id) {
        return R.ok(productFacade.getById(id));
    }

    /**
     * 返回当前多模块分层说明，方便对照工程结构。
     *
     * @return 模块说明
     */
    @GetMapping("/meta/modules")
    public R<Map<String, String>> modules() {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("demo-bom", "只管理版本，无业务代码");
        body.put("demo-api", "DTO + ProductFacade 接口");
        body.put("demo-service", "ProductFacadeImpl 业务实现");
        body.put("demo-web", "启动类 + Controller，端口 8005");
        body.put("dependency", "web → service → api；版本由 BOM import");
        return R.ok(body);
    }
}
