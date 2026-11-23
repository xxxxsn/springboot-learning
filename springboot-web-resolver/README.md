# springboot-web-resolver

> 编号：28 · 域 02 · **状态：已完成（2026-11-23）** · 端口：**8028**  
> 展开笔记：[docs/](docs/) · 自测：[api.http](api.http) · 总进度：[docs/进度.md](../docs/进度.md)

## 1. 适用场景

控制器里反复 `request.getAttribute("loginUser")` 之后，把当前用户收成 `@CurrentUser`。同时用返回值处理器把部分接口自动包进统一 `R`，没标注的接口保持原样。

## 2. 出现频率与推荐度

- 频率：高频
- 推荐度：推荐
- 整理优先级：P1

## 3. 依赖与版本

- `spring-boot-starter-web`
- `springboot-common` 的 `R` / `BusinessException`
- 不引入 Spring Security。用户来自演示请求头

详见 [docs/03-依赖与版本.md](docs/03-依赖与版本.md)。

## 4. 关键配置

端口 `8028`。请求头 `X-User-Id`、`X-User-Name`。没有额外的 `application` 业务配置。

详见 [docs/04-关键配置.md](docs/04-关键配置.md)。

## 5. 核心代码要点

| 类 | 作用 |
|---|---|
| `DemoUserFilter` | 请求头 → request 里的 `LoginUser`，不拦截 |
| `CurrentUserArgumentResolver` | `@CurrentUser`，`required` 默认 true |
| `ApiResultReturnValueHandler` | 包成 `R`；已经是 `R` 则跳过 |
| `ApiResultHandlerInstaller` | 插到 `@ResponseBody` 处理器之前 |

细节：[docs/05-核心代码要点.md](docs/05-核心代码要点.md)。

## 6. 验证步骤

```bash
mvn -pl springboot-web-resolver -am test
```

或启动后按 [api.http](api.http) 点。  
步骤：[docs/06-验证步骤.md](docs/06-验证步骤.md)。

## 怎么观察（学习用）

| 方式 | 看什么 |
|---|---|
| 接口优先 | `/me` 有无请求头、`/plain` 没有信封、`/already-wrapped` 不双层 |
| 日志 | 启动：`[resolver] ApiResult 返回值处理器已插入首位` |

总指南：[docs/学习观测指南.md](../docs/学习观测指南.md)
## 7. 常见坑与排障

见 [docs/07-常见坑与排障.md](docs/07-常见坑与排障.md)。

## 8. 对照方案

见 [docs/08-对照方案.md](docs/08-对照方案.md)。
