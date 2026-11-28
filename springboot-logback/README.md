# springboot-logback

> 编号：18 · 域 01 · **状态：已完成（2026-11-21）** · 端口：**8018**  
> 展开笔记：[docs/](docs/) · 自测：[api.http](api.http) · 总进度：[docs/进度.md](../docs/进度.md)

## 1. 适用场景

企业默认 Logback 落地：请求 MDC（traceId/userId）、按模块分文件、结构化 JSON 文件；`json` profile 切换 JSON 控制台。异步吞吐对比见 #19。

## 2. 出现频率与推荐度

- 频率：高频
- 推荐度：推荐
- 整理优先级：P0

## 3. 依赖与版本

- 默认 `spring-boot-starter-logging`（Logback），无需排除
- Boot 3.5 `StructuredLogEncoder`（logstash JSON）
- 配置：`logback-spring.xml`

## 4. 关键配置

见 [`logback-spring.xml`](src/main/resources/logback-spring.xml)：

- `app.log` 全量 Pattern
- `business.log` 业务包分文件
- `app.json.log` 结构化 JSON
- `springProfile=json` → 控制台 JSON

## 5. 核心代码要点

| 项 | 说明 |
|---|---|
| TraceIdFilter | MDC put/remove |
| BusinessLogService | additivity=false → business.log |
| StructuredLogEncoder | Boot 内置，无需 logstash-logback-encoder |

## 6. 验证步骤

```bash
cmd /c "mvn -pl springboot-logback -am spring-boot:run"
```

见 [docs/06-验证步骤.md](docs/06-验证步骤.md)。

## 怎么观察（学习用）

| 方式 | 看什么 |
|---|---|
| 日志优先 | `logs/springboot-logback/app.log`、`business.log`、`app.json.log`；控制台 MDC |
| 接口 | `/api/logback/sample`、`/business` 只负责打点 |

总指南：[docs/学习观测指南.md](../docs/学习观测指南.md)

## 7. 常见坑与排障

见 [docs/07-常见坑与排障.md](docs/07-常见坑与排障.md)。

## 8. 对照方案

见 [docs/08-对照方案.md](docs/08-对照方案.md)。