# springboot-web-interceptor

> 编号：27 · 域 02 · **状态：已完成（2026-11-21）** · 端口：**8027**  
> 展开笔记：[docs/](docs/) · 自测：[api.http](api.http) · 总进度：[docs/进度.md](../docs/进度.md)

## 1. 适用场景

需要在请求链路上做 Trace、轻量鉴权、访问耗时、登录用户上下文、Session/请求审计时，分清 Filter / Interceptor / Listener 该放哪一层。

## 2. 出现频率与推荐度

- 频率：高频
- 推荐度：推荐
- 整理优先级：P1

## 3. 依赖与版本

- `spring-boot-starter-web`
- `springboot-common`（统一 `R`）
- 用 `FilterRegistrationBean` 控制 Filter 顺序与 URL

## 4. 关键配置

```yaml
server.port: 8027
demo.interceptor.demo-token: demo-token-8027
```

## 5. 核心代码要点

| 层 | 类 | 职责 |
|---|---|---|
| Filter | `TraceIdFilter` | 最早种 traceId |
| Filter | `DemoAuthFilter` | `/secure/**` Token 校验 |
| Listener | `RequestAuditListener` | 请求 init/destroy |
| Listener | `DemoSessionListener` | 在线 Session 计数 |
| Interceptor | `LoginUserInterceptor` | 解析 `X-Demo-User` |
| Interceptor | `AccessLogInterceptor` | 耗时 + `X-Cost-Ms` |

## 6. 验证步骤

```bash
cmd /c "mvn -pl springboot-web-interceptor -am spring-boot:run"
```

见 [docs/06-验证步骤.md](docs/06-验证步骤.md)。

## 怎么观察（学习用）

| 方式 | 看什么 |
|---|---|
| 混合 | `GET /api/interceptor/chain` 看执行顺序 |
| 日志优先（细节） | `[access] METHOD uri status costMs`；`[session] sessionCreated`（需真实启动，MockMvc 看不全 Listener） |

总指南：[docs/学习观测指南.md](../docs/学习观测指南.md)

## 7. 常见坑与排障

见 [docs/07-常见坑与排障.md](docs/07-常见坑与排障.md)。

## 8. 对照方案

见 [docs/08-对照方案.md](docs/08-对照方案.md)。
