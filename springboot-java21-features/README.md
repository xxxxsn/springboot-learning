# springboot-java21-features

> 编号：3 · 域 01 · **状态：已完成（2026-11-20）** · 端口：**8003**  
> 展开笔记：[docs/](docs/) · 自测：[api.http](api.http) · 总进度：[docs/进度.md](../docs/进度.md)

## 1. 适用场景

在 Boot 项目里实操 Java 21：record、密封类、模式匹配 switch、文本块、Sequenced Collections。

## 2. 出现频率与推荐度

- 频率：高频
- 推荐度：推荐
- 整理优先级：P1

## 3. 依赖与版本

- JDK 21（父工程 `java.version=21`）
- `springboot-common` + `spring-boot-starter-web`

## 4. 关键配置

```yaml
server.port: 8003
```

## 5. 核心代码要点

- `Point` record：不可变 DTO
- `Shape` sealed interface + 嵌套 record：穷尽模式匹配
- 文本块：多行 SQL/JSON
- `SequencedCollection#getFirst/getLast/reversed`

细节见 [docs/05-核心代码要点.md](docs/05-核心代码要点.md)。

## 6. 验证步骤

启动后执行 `api.http`，或访问 `GET /api/java21` 一次看全。  
见 [docs/06-验证步骤.md](docs/06-验证步骤.md)。

## 怎么观察（学习用）

| 方式 | 看什么 |
|---|---|
| 接口优先 | `GET /api/java21` 各特性返回 |

总指南：[docs/学习观测指南.md](../docs/学习观测指南.md)

## 7. 常见坑与排障

见 [docs/07-常见坑与排障.md](docs/07-常见坑与排障.md)。

## 8. 对照方案

见 [docs/08-对照方案.md](docs/08-对照方案.md)。
