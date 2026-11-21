package com.xiesn.springboot.web.restful.service;

import com.xiesn.springboot.common.exception.BusinessException;
import com.xiesn.springboot.common.page.PageQuery;
import com.xiesn.springboot.common.page.PageResult;
import com.xiesn.springboot.common.result.CommonErrorCode;
import com.xiesn.springboot.web.restful.domain.Article;
import com.xiesn.springboot.web.restful.dto.ArticleCreateRequest;
import com.xiesn.springboot.web.restful.dto.ArticlePatchRequest;
import com.xiesn.springboot.web.restful.dto.ArticleReplaceRequest;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 文章内存仓储：只服务 REST 演示，不做持久化。
 */
@Service
public class ArticleService {

    private final ConcurrentHashMap<Long, Article> store = new ConcurrentHashMap<>();
    private final AtomicLong idSeq = new AtomicLong(1000);

    /**
     * 预置几条样例数据，方便启动后立刻点 api.http。
     */
    @PostConstruct
    void seed() {
        saveNew("REST 入门", "资源命名与 HTTP 动词", "PUBLISHED");
        saveNew("统一响应 R", "code / message / data", "PUBLISHED");
        saveNew("草稿文章", "尚未发布", "DRAFT");
    }

    /**
     * 分页查询，可按标题关键字过滤。
     *
     * @param query   分页
     * @param keyword 标题关键字，可空
     * @return 分页结果
     */
    public PageResult<Article> page(PageQuery query, String keyword) {
        List<Article> filtered = store.values().stream()
                .filter(item -> !StringUtils.hasText(keyword)
                        || item.title().toLowerCase().contains(keyword.toLowerCase()))
                .sorted(Comparator.comparing(Article::id))
                .toList();
        long total = filtered.size();
        int from = (int) Math.min(query.offset(), total);
        int to = (int) Math.min(from + query.safePageSize(), total);
        return PageResult.of(query, total, filtered.subList(from, to));
    }

    /**
     * 按 id 查找。
     *
     * @param id 主键
     * @return 文章，可能为空
     */
    public Optional<Article> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    /**
     * 按 id 获取，不存在则抛业务异常（HTTP 层仍可能是 200，见 #25）。
     *
     * @param id 主键
     * @return 文章
     */
    public Article requireById(Long id) {
        return findById(id)
                .orElseThrow(() -> new BusinessException(CommonErrorCode.NOT_FOUND, "文章不存在: " + id));
    }

    /**
     * 创建文章，默认 DRAFT。
     *
     * @param request 创建入参
     * @return 新建文章
     */
    public Article create(ArticleCreateRequest request) {
        return saveNew(request.getTitle().trim(), request.getContent().trim(), "DRAFT");
    }

    /**
     * 全量替换。
     *
     * @param id      主键
     * @param request 替换入参
     * @return 更新后文章
     */
    public Article replace(Long id, ArticleReplaceRequest request) {
        requireById(id);
        Article updated = new Article(
                id,
                request.getTitle().trim(),
                request.getContent().trim(),
                request.getStatus(),
                Instant.now());
        store.put(id, updated);
        return updated;
    }

    /**
     * 部分更新：null 字段保持原值。
     *
     * @param id      主键
     * @param request 补丁入参
     * @return 更新后文章
     */
    public Article patch(Long id, ArticlePatchRequest request) {
        Article current = requireById(id);
        String title = request.getTitle() == null ? current.title() : request.getTitle().trim();
        String content = request.getContent() == null ? current.content() : request.getContent().trim();
        String status = request.getStatus() == null ? current.status() : request.getStatus();
        if (!StringUtils.hasText(title) || !StringUtils.hasText(content)) {
            throw new BusinessException(CommonErrorCode.BAD_REQUEST, "title/content 不能更新为空");
        }
        Article updated = new Article(id, title, content, status, Instant.now());
        store.put(id, updated);
        return updated;
    }

    /**
     * 删除文章。
     *
     * @param id 主键
     * @return 是否删除成功（false 表示本来就不存在）
     */
    public boolean delete(Long id) {
        return store.remove(id) != null;
    }

    private Article saveNew(String title, String content, String status) {
        long id = idSeq.incrementAndGet();
        Article article = new Article(id, title, content, status, Instant.now());
        store.put(id, article);
        return article;
    }
}
