package com.xiesn.springboot.web.restful.web;

import com.xiesn.springboot.common.page.PageQuery;
import com.xiesn.springboot.common.page.PageResult;
import com.xiesn.springboot.common.result.CommonErrorCode;
import com.xiesn.springboot.common.result.R;
import com.xiesn.springboot.web.restful.domain.Article;
import com.xiesn.springboot.web.restful.dto.ArticleCreateRequest;
import com.xiesn.springboot.web.restful.dto.ArticlePatchRequest;
import com.xiesn.springboot.web.restful.dto.ArticleReplaceRequest;
import com.xiesn.springboot.web.restful.service.ArticleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * 文章资源控制器：演示 REST 动词、状态码、Location 与统一 {@code R}。
 */
@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    /**
     * 注入文章服务。
     *
     * @param articleService 文章服务
     */
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    /**
     * 分页列表：集合资源用复数名词 + GET。
     *
     * @param query   分页
     * @param keyword 标题关键字
     * @return 统一分页响应
     */
    @GetMapping
    public R<PageResult<Article>> list(PageQuery query,
                                       @RequestParam(required = false) String keyword) {
        return R.ok(articleService.page(query, keyword));
    }

    /**
     * 单资源查询：存在 200 + R；不存在 404 + R（业务码与 HTTP 状态对齐）。
     *
     * @param id 主键
     * @return 响应实体
     */
    @GetMapping("/{id}")
    public ResponseEntity<R<Article>> get(@PathVariable Long id) {
        return articleService.findById(id)
                .<ResponseEntity<R<Article>>>map(article -> ResponseEntity.ok(R.ok(article)))
                .orElseGet(() -> ResponseEntity.status(404)
                        .body(R.fail(CommonErrorCode.NOT_FOUND, "文章不存在: " + id)));
    }

    /**
     * 创建：201 Created + Location 指向新资源。
     *
     * @param request    创建体
     * @param uriBuilder URI 构造器
     * @return 201 响应
     */
    @PostMapping
    public ResponseEntity<R<Article>> create(@Valid @RequestBody ArticleCreateRequest request,
                                             UriComponentsBuilder uriBuilder) {
        Article created = articleService.create(request);
        URI location = uriBuilder.path("/api/articles/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(R.ok(created));
    }

    /**
     * 全量替换：客户端必须提供完整字段。
     *
     * @param id      主键
     * @param request 替换体
     * @return 统一响应
     */
    @PutMapping("/{id}")
    public R<Article> replace(@PathVariable Long id,
                              @Valid @RequestBody ArticleReplaceRequest request) {
        return R.ok(articleService.replace(id, request));
    }

    /**
     * 部分更新：只改传入的非 null 字段。
     *
     * @param id      主键
     * @param request 补丁体
     * @return 统一响应
     */
    @PatchMapping("/{id}")
    public R<Article> patch(@PathVariable Long id,
                            @Valid @RequestBody ArticlePatchRequest request) {
        return R.ok(articleService.patch(id, request));
    }

    /**
     * 删除：成功 204 无 body；不存在 404 + R。
     *
     * @param id 主键
     * @return 响应实体
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<R<Void>> delete(@PathVariable Long id) {
        if (!articleService.delete(id)) {
            return ResponseEntity.status(404)
                    .body(R.fail(CommonErrorCode.NOT_FOUND, "文章不存在: " + id));
        }
        return ResponseEntity.noContent().build();
    }
}
