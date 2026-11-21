# springboot-log4j2

> 编号：19 · 域 01 · **状态：已完成（2026-11-21）** · 端口：**8019**  
> 展开笔记：[docs/](docs/) · 自测：[api.http](api.http) · 总进度：[docs/进度.md](../docs/进度.md)

## 1. 适用场景

需要把默认 Logback 换成 Log4j2，并对比 **异步日志** 与同步写盘；与 #18 logback（MDC/JSON/分文件深挖）对照选型。

## 2. 出现频率与推荐度

- 频率：常见
- 推荐度：对照
- 整理优先级：P2

## 3. 依赖与版本

- 排除 `spring-boot-starter-logging`，引入 `spring-boot-starter-log4j2`
- `com.lmax:disruptor`（AsyncLogger 必需）
- 配置文件：`log4j2-spring.xml`（不要用过早加载的纯 `log4j2.xml` 若要用 Spring 扩展）

## 4. 关键配置

见 [`log4j2-spring.xml`](src/main/resources/log4j2-spring.xml)：Console + RollingFile + Async / AsyncLogger。

## 5. 核心代码要点

| 项 | 说明 |
|---|---|
| 排除 Logback | common/web/test 均 exclude logging |
| AsyncLogger | `com.xiesn.springboot.log4j2.demo` |
| 同步对照 | `...log4j2.sync.SyncProbe` 走 Root |
| MDC | `TraceIdFilter` + `%X{traceId}` |

## 6. 验证步骤

```bash
cmd /c "mvn -pl springboot-log4j2 -am spring-boot:run"
```

`GET /api/log4j2/status` 应 `log4j2=true`；对比 async-burst / sync-burst 的 `costMs`。

## 7. 常见坑与排障

见 [docs/07-常见坑与排障.md](docs/07-常见坑与排障.md)。

## 8. 对照方案

见 [docs/08-对照方案.md](docs/08-对照方案.md)。