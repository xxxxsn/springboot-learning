# springboot-web-cors

> 编号：29 · 域 02 · **状态：已完成（2026-11-24）** · 端口：**8029**  
> 展开笔记：[docs/](docs/) · 自测：[api.http](api.http) · 总进度：[docs/进度.md](../docs/进度.md)

## 1. 适用场景

前后端不同源时，浏览器要先看响应里的 CORS 头才把结果交给页面。服务端要声明允许哪个源、哪些方法，以及带 Cookie 时能不能用 `*`。

## 2. 出现频率与推荐度

- 频率：高频
- 推荐度：推荐
- 整理优先级：P1

## 3. 依赖与版本

- `spring-boot-starter-web`（Spring Framework 6.2 的 `CorsRegistry` / `DefaultCorsProcessor`）
- `springboot-common` 的统一 `R`
- 不引入 Spring Security 的 CORS 过滤器

详见 [docs/03-依赖与版本.md](docs/03-依赖与版本.md)。

## 4. 关键配置

端口 `8029`。全局只放行 `https://admin.example.com`，`allowCredentials=true`，预检缓存 1800 秒。`/shop` 再用 `@CrossOrigin` 追加商城源。

详见 [docs/04-关键配置.md](docs/04-关键配置.md)。

## 5. 核心代码要点

| 类 | 作用 |
|---|---|
| `CorsDemoConfig` | `/api/cors/**` 的全局白名单、方法、暴露头、凭证 |
| `CorsDemoController` | 总览、写接口预检、`/shop` 方法级源站 |
| `PlainPingController` | 映射之外：实际请求不写 Allow-Origin，预检 403 |
| `CorsOrigins` | 测试和配置共用的源站常量 |

细节：[docs/05-核心代码要点.md](docs/05-核心代码要点.md)。

## 6. 验证步骤

```bash
mvn -pl springboot-web-cors -am test
```

或启动后按 [api.http](api.http) 带 `Origin` 点。  
步骤：[docs/06-验证步骤.md](docs/06-验证步骤.md)。

## 怎么观察（学习用）

| 方式 | 看什么 |
|---|---|
| 接口优先 | 响应头 `Access-Control-Allow-Origin` / `Allow-Credentials` / 预检的 `Allow-Methods` |
| 日志 | 拒绝时 `DefaultCorsProcessor`：`Reject: '...' origin is not allowed` |

总指南：[docs/学习观测指南.md](../docs/学习观测指南.md)
## 7. 常见坑与排障

见 [docs/07-常见坑与排障.md](docs/07-常见坑与排障.md)。

## 8. 对照方案

见 [docs/08-对照方案.md](docs/08-对照方案.md)。
