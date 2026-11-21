# springboot-web-validation

> 编号：24 · 域 02 · **状态：已完成（2026-11-21）** · 端口：**8024**  
> 展开笔记：[docs/](docs/) · 自测：[api.http](api.http) · 总进度：[docs/进度.md](../docs/进度.md)

## 1. 适用场景

企业 API 入参校验要可复用、可分组、错误能落到字段级高亮；非 HTTP 入口（MQ/Job）也要同一套约束。

## 2. 出现频率与推荐度

- 频率：高频
- 推荐度：推荐
- 整理优先级：P0

## 3. 依赖与版本

- `spring-boot-starter-validation`（Hibernate Validator）
- `springboot-common`：`R` / 基础异常
- 自定义注解放本模块 `validation` 包（实战可下沉 common）

## 4. 关键配置

`server.port=8024`；无额外校验开关。

## 5. 核心代码要点

| 项 | 说明 |
|---|---|
| `@Mobile` / `@EnumValue` | 企业常封装业务注解 |
| `Create` / `Update` | 同一 DTO 分组 |
| `ValidationExceptionAdvice` | 多字段错误进 `R.data` |
| `ManualValidationDemo` | 注入 `Validator` 模拟 MQ |

## 6. 验证步骤

```bash
cmd /c "mvn -pl springboot-web-validation -am spring-boot:run"
```

见 [docs/06-验证步骤.md](docs/06-验证步骤.md)。

## 7. 常见坑与排障

见 [docs/07-常见坑与排障.md](docs/07-常见坑与排障.md)。

## 8. 对照方案

见 [docs/08-对照方案.md](docs/08-对照方案.md)。
