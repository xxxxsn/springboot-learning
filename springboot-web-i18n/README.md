# springboot-web-i18n

> 编号：31 · 域 02 · **状态：已完成** · 端口：**8031**  
> 展开笔记：[docs/](docs/) · 自测：[api.http](api.http) · 总进度：[docs/进度.md](../docs/进度.md)

## 1. 适用场景

前后端分离时，接口失败不要回中文句子。`message` 放 i18n key，后台 vue-i18n、前台 Nuxt i18n 各自翻译。服务端模板或邮件才用 `MessageSource`。

## 2. 出现频率与推荐度

- 频率：高频
- 推荐度：推荐（key）；`MessageSource` 为对照
- 整理优先级：P0

## 3. 依赖与版本

- Spring Boot 3.5.16 自带的 `MessageSource`（`spring-boot-starter-web`）
- `spring-boot-starter-validation`：演示参数校验收成同一个 key
- 不引入 vue 或 Nuxt 工程。前端文案放在 `src/main/resources/frontend-catalog/`

详见 [docs/03-依赖与版本.md](docs/03-依赖与版本.md)。

## 4. 关键配置

端口 `8031`。`spring.messages.basename=messages`，`fallback-to-system-locale=false`。  
完整说明：[docs/04-关键配置.md](docs/04-关键配置.md)。

## 5. 核心代码要点

| 类 | 作用 |
|---|---|
| `I18nErrorCode` | 数字码 + key，例如 `41001` / `auth.login.invalid_credentials` |
| `I18nDemoService` | 登录口令、扩展名白名单 |
| `I18nExceptionHandler` | 校验失败统一成 `common.param_invalid`，盖住 common 的中文拼接 |
| `I18nMessageService` | 对照：按 `Accept-Language` 译成句子 |

细节：[docs/05-核心代码要点.md](docs/05-核心代码要点.md)。

## 6. 验证步骤

`mvn -pl springboot-web-i18n -am test`，或启动后按 [api.http](api.http) 点。教学口令是 `secret`。  
步骤：[docs/06-验证步骤.md](docs/06-验证步骤.md)。

## 怎么观察（学习用）

| 方式 | 看什么 |
|---|---|
| 接口优先 | 换 `Accept-Language` / 请求头，对照错误 key 与文案 |
| 配置 | `messages*.properties`、前端 catalog 样例 |

总指南：[docs/学习观测指南.md](../docs/学习观测指南.md)

## 7. 常见坑与排障

见 [docs/07-常见坑与排障.md](docs/07-常见坑与排障.md)。

## 8. 对照方案

见 [docs/08-对照方案.md](docs/08-对照方案.md)。
