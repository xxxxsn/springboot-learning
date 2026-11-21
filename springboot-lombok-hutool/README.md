# springboot-lombok-hutool

> 编号：8 · 域 01 · **状态：已完成（2026-11-21）** · 端口：**8008**  
> 展开笔记：[docs/](docs/) · 自测：[api.http](api.http) · 总进度：[docs/进度.md](../docs/进度.md)

## 1. 适用场景

日常减少样板代码（Lombok）、字符串/日期/ID/Bean 工具（Hutool）、不可变集合与本地小缓存（Guava）。重点看清 Lombok 与工具选型的坑，而不是背 API 清单。

## 2. 出现频率与推荐度

- 频率：高频
- 推荐度：推荐
- 整理优先级：P1

## 3. 依赖与版本

- `springboot-common` + `spring-boot-starter-web`
- Hutool `5.8.35`（父 POM）、Guava `33.4.0-jre`、Lombok 跟 Boot BOM（optional，打包排除）

## 4. 关键配置

```yaml
server:
  port: 8008
```

无特殊业务配置；Lombok 需 IDE 注解处理开启。

## 5. 核心代码要点

| 类别 | 类 | 作用 |
|---|---|---|
| Lombok | `UserDto` / `BuilderUser` / `DataEmployee` / `SafeEmployee` | 常用注解 + Jackson/equals 坑 |
| Lombok | `LombokDemoService` + `@Slf4j` | 注解汇总与对比 |
| Hutool | `HutoolDemoService` | Str/Date/Id/Digest/Bean/JSON |
| Guava | `GuavaDemoService` | Immutable/Partition/Joiner/Preconditions/Cache |

## 6. 验证步骤

1. 启动 `LombokHutoolApplication`
2. `GET /api/lombok/overview`、`POST /api/lombok/jackson`、`GET /api/lombok/equals`
3. `GET /api/hutool/all`、`GET /api/guava/all`

详见 [docs/06-验证步骤.md](docs/06-验证步骤.md)。

## 7. 常见坑与排障

见 [docs/07-常见坑与排障.md](docs/07-常见坑与排障.md)。

## 8. 对照方案

见 [docs/08-对照方案.md](docs/08-对照方案.md)。
