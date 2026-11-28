# springboot-web-jackson

> 编号：26 · 域 02 · **状态：已完成（2026-11-23）** · 端口：**8026**  
> 展开笔记：[docs/](docs/) · 自测：[api.http](api.http) · 总进度：[docs/进度.md](../docs/进度.md)

## 1. 适用场景

前后端分离时，接口 JSON 要稳定：时间不要一会儿时间戳一会儿 ISO、雪花 ID 不能被 JS 吃掉精度、枚举对外用数字码、空字段按约定省略。

## 2. 出现频率与推荐度

- 频率：高频
- 推荐度：推荐
- 整理优先级：P0

## 3. 依赖与版本

- `spring-boot-starter-web` 自带的 Jackson 2（Boot 3.5.16）
- `springboot-common` 的统一 `R`
- 不额外引入 jackson-datatype，Java 8 时间模块由 Boot 自动注册

详见 [docs/03-依赖与版本.md](docs/03-依赖与版本.md)。

## 4. 关键配置

端口 `8026`。`spring.jackson.date-format` 只作用于 `java.util.Date`；`LocalDateTime` 由 `Jackson2ObjectMapperBuilderCustomizer` 补上 `yyyy-MM-dd HH:mm:ss`。全局 `default-property-inclusion: non_null`。

详见 [docs/04-关键配置.md](docs/04-关键配置.md)。

## 5. 核心代码要点

| 类 | 作用 |
|---|---|
| `JacksonDemoConfig` | 全局 `LocalDateTime` 格式 |
| `EnterpriseOrder` | 主键转字符串、`paidAt` 覆盖格式、`channel` 保留 null |
| `OrderStatus` | `@JsonValue` 写数字码，读入兼容码和枚举名 |
| `PlainOrder` | 无字段注解，枚举仍是名字 |
| `OrderJacksonService` | 同一数据再用默认 ObjectMapper 写一份文本 |

细节：[docs/05-核心代码要点.md](docs/05-核心代码要点.md)。

## 6. 验证步骤

```bash
mvn -pl springboot-web-jackson -am test
```

或启动后按 [api.http](api.http) 点 `GET /api/jackson/overview`。  
步骤：[docs/06-验证步骤.md](docs/06-验证步骤.md)。

## 怎么观察（学习用）

| 方式 | 看什么 |
|---|---|
| 接口优先 | `enterprise` 与 `plain` 的 id、status、时间、null；`rawPlainJson` 是默认 ObjectMapper |

总指南：[docs/学习观测指南.md](../docs/学习观测指南.md)

## 7. 常见坑与排障

见 [docs/07-常见坑与排障.md](docs/07-常见坑与排障.md)。

## 8. 对照方案

见 [docs/08-对照方案.md](docs/08-对照方案.md)。
