# springboot-web-exception

> 编号：25 · 域 02 · **状态：已完成（2026-11-21）** · 端口：**8025**  
> 展开笔记：[docs/](docs/) · 自测：[api.http](api.http) · 总进度：[docs/进度.md](../docs/进度.md)

## 1. 适用场景

企业 API 需要统一异常出口：业务码、HTTP 状态、traceId、字段错误、系统异常脱敏；可在「国内信封」与 RFC7807 ProblemDetail 间切换。

## 2. 出现频率与推荐度

- 频率：高频
- 推荐度：推荐
- 整理优先级：P0

## 3. 依赖与版本

- `spring-boot-starter-web` + `validation`
- `springboot-common`：`BusinessException` / `ErrorCode` / `AssertUtils`
- 本模块 `support/*` 为可下沉 framework 的封装样板（不改 common 默认处理器）

## 4. 关键配置

```yaml
demo.exception.style: ENVELOPE   # 或 PROBLEM_DETAIL（profile=problem）
spring.mvc.throw-exception-if-no-handler-found: true
spring.web.resources.add-mappings: false
```

## 5. 核心代码要点

| 项 | 说明 |
|---|---|
| `HttpStatusErrorCode` + `OrderErrorCode` | 域错误码自带 HTTP 状态 |
| `ErrorHttpStatusMapper` | 通用码 / 后三位对齐 HTTP |
| `ErrorResponseFactory` | 信封 `ErrorBody` 或 `ProblemDetail` |
| `EnterpriseExceptionAdvice` | 高于 common；校验 errors[]；兜底脱敏 |
| `TraceIdFilter` | MDC + 响应头 |

## 6. 验证步骤

```bash
cmd /c "mvn -pl springboot-web-exception -am spring-boot:run"
```

见 [docs/06-验证步骤.md](docs/06-验证步骤.md)。

## 怎么观察（学习用）

| 方式 | 看什么 |
|---|---|
| 混合 | 响应对齐 HTTP + `traceId`；`/boom` 响应脱敏 |
| 日志优先（细节） | `[exception]` 业务 warn；`/boom` **堆栈只在日志** |

总指南：[docs/学习观测指南.md](../docs/学习观测指南.md)

## 7. 常见坑与排障

见 [docs/07-常见坑与排障.md](docs/07-常见坑与排障.md)。

## 8. 对照方案

见 [docs/08-对照方案.md](docs/08-对照方案.md)。
