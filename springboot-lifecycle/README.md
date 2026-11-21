# springboot-lifecycle

> 编号：14 · 域 01 · **状态：已完成（2026-11-21）** · 端口：**8014**  
> 展开笔记：[docs/](docs/) · 自测：[api.http](api.http) · 总进度：[docs/进度.md](../docs/进度.md)

## 1. 适用场景

要搞清 Boot 从启动到停机各钩子的真实顺序：RunListener、Initializer、应用事件、Bean 回调、Runner、SmartLifecycle、优雅停机。

## 2. 出现频率与推荐度

- 频率：高频
- 推荐度：推荐
- 整理优先级：P1

## 3. 依赖与版本

- `springboot-common` + `spring-boot-starter-web`
- Boot 3.5.16；优雅停机配置见官方 Graceful Shutdown 文档
- `SpringApplicationRunListener` / `ApplicationContextInitializer` 通过 `META-INF/spring.factories` 注册（与 `AutoConfiguration.imports` 不同）

## 4. 关键配置

```yaml
server:
  port: 8014
  shutdown: graceful          # Boot 3.5 默认已是；显式写出
spring:
  lifecycle:
    timeout-per-shutdown-phase: 30s
```

## 5. 核心代码要点

| 类别 | 类 | 作用 |
|---|---|---|
| 时间线 | `LifecycleTimeline` | 静态收集，供启动早期钩子写入 |
| 启动 | `DemoSpringApplicationRunListener` | starting → ready |
| 启动 | `DemoApplicationContextInitializer` | refresh 前写属性 |
| Runner | `DemoApplicationRunner` / `DemoCommandLineRunner` | `@Order` 控制顺序 |
| 事件 | `DemoAppEventListeners` | Refreshed / Started / Ready / Closed |
| Bean | `DemoLifecycleBean` | Aware / PostConstruct / Init / PreDestroy |
| Smart | `DemoSmartLifecycle` | start / stop(callback) |
| 观测 | `LifecycleController` | `/timeline` `/summary` `/status` |
| 优雅停机 | `SlowRequestController` | 慢请求配合停机观察 |

## 6. 验证步骤

1. 启动 `LifecycleApplication`
2. `GET /api/lifecycle/timeline` 查看顺序
3. `GET /api/lifecycle/status` 确认 `initializerReady=true`
4. （可选）`GET /api/lifecycle/slow?seconds=8` 后停止进程，看日志中 `SmartLifecycle.stop` / `@PreDestroy`

详见 [docs/06-验证步骤.md](docs/06-验证步骤.md)。

## 怎么观察（学习用）

| 方式 | 看什么 |
|---|---|
| 日志优先 | 启动/停机控制台 `[lifecycle] seq=... phase=...` |
| 接口 | `GET /api/lifecycle/timeline` 只是汇总，顺序以日志为准 |

总指南：[docs/学习观测指南.md](../docs/学习观测指南.md)
## 7. 常见坑与排障

见 [docs/07-常见坑与排障.md](docs/07-常见坑与排障.md)。

## 8. 对照方案

见 [docs/08-对照方案.md](docs/08-对照方案.md)。
