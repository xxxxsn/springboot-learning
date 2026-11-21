package com.xiesn.springboot.web.restful.web;

import com.xiesn.springboot.common.exception.BusinessException;
import com.xiesn.springboot.common.result.CommonErrorCode;
import com.xiesn.springboot.common.result.R;
import com.xiesn.springboot.web.restful.domain.Article;
import com.xiesn.springboot.web.restful.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * REST 约定总览与两种「失败表达」对照。
 */
@RestController
@RequestMapping("/api/restful")
public class RestfulOverviewController {

    private final ArticleService articleService;

    /**
     * 注入文章服务。
     *
     * @param articleService 文章服务
     */
    public RestfulOverviewController(ArticleService articleService) {
        this.articleService = articleService;
    }

    /**
     * 返回本模块约定速查表，便于对照 api.http。
     *
     * @return 约定说明
     */
    @GetMapping("/overview")
    public R<Map<String, Object>> overview() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("module", "springboot-web-restful");
        data.put("port", 8023);
        data.put("resource", "/api/articles");
        data.put("verbs", List.of(
                Map.of("method", "GET", "path", "/api/articles", "httpStatus", 200, "note", "分页集合"),
                Map.of("method", "GET", "path", "/api/articles/{id}", "httpStatus", "200/404", "note", "单资源"),
                Map.of("method", "POST", "path", "/api/articles", "httpStatus", 201, "note", "Created + Location"),
                Map.of("method", "PUT", "path", "/api/articles/{id}", "httpStatus", 200, "note", "全量替换"),
                Map.of("method", "PATCH", "path", "/api/articles/{id}", "httpStatus", 200, "note", "部分更新"),
                Map.of("method", "DELETE", "path", "/api/articles/{id}", "httpStatus", "204/404", "note", "成功无 body")
        ));
        data.put("envelope", Map.of(
                "type", "R<code,message,data>",
                "successCode", 0,
                "tip", "国内项目常「HTTP 200 + 业务 code」；本模块在关键路径上刻意对齐 HTTP 语义"
        ));
        data.put("boundary", Map.of(
                "validationDeepDive", "#24 springboot-web-validation",
                "problemDetail", "#25 springboot-web-exception",
                "jackson", "#26 springboot-web-jackson"
        ));
        return R.ok(data);
    }

    /**
     * 信封风格：无论资源是否存在，HTTP 多为 200，靠 {@code R.code} 区分。
     * <p>这里故意抛 {@link BusinessException}，由 common 全局处理器转成 {@code R}，HTTP 仍是 200。</p>
     *
     * @param id 文章 id
     * @return 统一响应
     */
    @GetMapping("/styles/envelope/{id}")
    public R<Article> envelopeStyle(@PathVariable Long id) {
        return R.ok(articleService.requireById(id));
    }

    /**
     * HTTP 状态风格：资源不存在时返回 404，body 仍包一层 {@code R} 方便前端统一解析。
     *
     * @param id 文章 id
     * @return 响应实体
     */
    @GetMapping("/styles/status/{id}")
    public ResponseEntity<R<Article>> statusStyle(@PathVariable Long id) {
        return articleService.findById(id)
                .<ResponseEntity<R<Article>>>map(article -> ResponseEntity.ok(R.ok(article)))
                .orElseGet(() -> ResponseEntity.status(404)
                        .body(R.fail(CommonErrorCode.NOT_FOUND, "文章不存在: " + id)));
    }
}
