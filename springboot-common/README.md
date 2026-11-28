# springboot-common

> 编号：1 · 域 01 · **状态：已完成（2026-11-20）**  
> 展开笔记：[docs/](docs/) · 总进度：[docs/进度.md](../docs/进度.md)

## 1. 适用场景

所有业务模块的公共底座：统一返回、错误码、业务异常、实体基类、分页对象、断言与字符串工具。

## 2. 出现频率与推荐度

- 频率：高频
- 推荐度：推荐
- 整理优先级：P0

## 3. 依赖与版本

- Spring Boot 3.5.16 / Java 21（父工程）
- `spring-boot-starter-web` + `validation`
- Boot 3 `AutoConfiguration.imports` → `CommonAutoConfiguration`

## 4. 关键配置

库模块，无独立端口。业务模块引入依赖即可启用全局异常处理。

## 5. 核心代码要点

| 包 | 类 | 作用 |
|---|---|---|
| result | `R` / `ErrorCode` / `CommonErrorCode` | 统一响应与错误码 |
| exception | `BusinessException` / `GlobalExceptionHandler` | 业务异常与全局转换 |
| entity | `BaseEntity` | id/审计/逻辑删除 |
| page | `PageQuery` / `PageResult` | 分页 |
| util | `AssertUtils` / `StringUtils` | 工具 |
| config | `CommonAutoConfiguration` | 自动配置入口 |

细节见 [docs/05-核心代码要点.md](docs/05-核心代码要点.md)。

## 6. 验证步骤

随 `springboot-hello` 启动后访问 `/api/hello/boom?keyword=bad`，应返回 `code=1000`。  
见 [docs/06-验证步骤.md](docs/06-验证步骤.md)。

## 怎么观察（学习用）

| 方式 | 看什么 |
|---|---|
| 接口 | 随 hello / jasypt / web-* 一起看统一 `R` 与异常包装 |
| 日志 | 其它模块抛 `BusinessException` 时 common 处理器打 warn |

总指南：[docs/学习观测指南.md](../docs/学习观测指南.md)

## 7. 常见坑与排障

见 [docs/07-常见坑与排障.md](docs/07-常见坑与排障.md)。

## 8. 对照方案

见 [docs/08-对照方案.md](docs/08-对照方案.md)。
