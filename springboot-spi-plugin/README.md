 springboot-spi-plugin

> 编号：16 · 域 01 · **状态：已完成（2026-11-21）** · 端口：**8016**  
> 展开笔记：[docs/](docs/) · 自测：[api.http](api.http) · 总进度：[docs/进度.md](../docs/进度.md)

## 1. 适用场景

同一业务能力有多个实现（短信/邮件/推送），需要可插拔发现与按名调用；理解 Java SPI、Spring Bean 策略、SpringFactoriesLoader 的差异。热插拔独立 jar 见 #415 PF4J。

## 2. 出现频率与推荐度

- 频率：常见
- 推荐度：了解
- 整理优先级：P2

## 3. 依赖与版本

- `springboot-common` + `spring-boot-starter-web`
- 无额外第三方插件框架（刻意对照 #415）

## 4. 关键配置

```yaml
server:
  port: 8016
```

注册文件：

- `META-INF/services/...MessagePlugin`（Java SPI）
- `META-INF/spring.factories`（自定义 `PluginExtension`）

## 5. 核心代码要点

| 机制 | 关键类 | 发现方式 |
|---|---|---|
| Java SPI | `SmsMessagePlugin` / `EmailMessagePlugin` + `JavaSpiPluginLoader` | `ServiceLoader` |
| Spring Bean | `PushMessagePlugin` / `WechatMessagePlugin` + `SpringBeanPluginRegistry` | `@Component` + `List` 注入 |
| Spring Factories | `Audit/MetricsPluginExtension` + `SpringFactoriesExtensionLoader` | `SpringFactoriesLoader` |

## 6. 验证步骤

1. 启动 `SpiPluginApplication`
2. `GET /api/spi/overview`
3. `GET /api/spi/java/sms`、`GET /api/spi/spring/push`

## 怎么观察（学习用）

| 方式 | 看什么 |
|---|---|
| 接口优先 | `GET /api/spi/overview` |
| 配置 | `META-INF/services`、factories 文件是否被加载 |

总指南：[docs/学习观测指南.md](../docs/学习观测指南.md)
## 7. 常见坑与排障

见 [docs/07-常见坑与排障.md](docs/07-常见坑与排障.md)。

## 8. 对照方案

见 [docs/08-对照方案.md](docs/08-对照方案.md)。
