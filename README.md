# springboot-learning

Spring Boot 3 + Java 21 企业技术百科（可运行案例库）。

## 先看进度

隔一段时间回来，**先打开** → [`docs/进度.md`](docs/进度.md)  
怎么用日志/接口观察模块 → [`docs/学习观测指南.md`](docs/学习观测指南.md)

当前已实现：

| # | 模块 | 端口 | 状态 | 核心内容 |
|---|---|---|---|---|
| 1 | `springboot-common` | — | 已完成 | 统一 `R`、错误码、全局异常、分页、工具 + Boot3 自动配置 |
| 2 | `springboot-hello` | 8002 | 已完成 | Boot3 起步 + `AutoConfiguration.imports` 自定义自动配置 |
| 3 | `springboot-java21-features` | 8003 | 已完成 | record / sealed / 模式匹配 / 文本块 / Sequenced |
| 5 | `springboot-maven-multimodule` | 8005 | 已完成 | 多模块 BOM + api/service/web 分层 + Nexus 约定 |
| 8 | `springboot-lombok-hutool` | 8008 | 已完成 | Lombok 坑 + Hutool 高频工具 + Guava 经典用法 |
| 9 | `springboot-profiles` | 8009 | 已完成 | 多环境、Profile 组、config.import、`@Profile`、生效配置观测 |
| 11 | `springboot-jasypt` | 8011 | 已完成 | 配置 `ENC(...)` 加解密（jasypt-spring-boot） |
| 14 | `springboot-lifecycle` | 8014 | 已完成 | 启动/停机时间线：RunListener、Runner、事件、Bean、SmartLifecycle、优雅停机 |
| 16 | `springboot-spi-plugin` | 8016 | 已完成 | Java SPI / Spring Bean 策略 / SpringFactoriesLoader 插件发现 |
| 17 | `springboot-docker-compose` | 8017 | 已完成 | Boot 开发期自动 docker compose + Redis Service Connection |
| 18 | `springboot-logback` | 8018 | 已完成 | MDC、业务分文件、Structured JSON |
| 19 | `springboot-log4j2` | 8019 | 已完成 | 排除 Logback、Log4j2 异步对比、轻量 MDC |
| 23 | `springboot-web-restful` | 8023 | 已完成 | REST 动词/状态码/Location + 统一 R；信封 vs HTTP 状态对照 |
| 24 | `springboot-web-validation` | 8024 | 已完成 | 自定义注解、分组、嵌套、手动 Validator；错误列表进 R |
| 25 | `springboot-web-exception` | 8025 | 已完成 | 企业异常：码→HTTP、traceId、信封/ProblemDetail、脱敏兜底 |
| 26 | `springboot-web-jackson` | 8026 | 已完成 | 日期格式、Long 精度、枚举码、空值；对照默认 ObjectMapper |
| 27 | `springboot-web-interceptor` | 8027 | 已完成 | Filter/Interceptor/Listener 职责对照、Trace、轻量鉴权、耗时 |
| 28 | `springboot-web-resolver` | 8028 | 已完成 | `@CurrentUser` 参数解析；`@ApiResult` 返回值包装 |
| 29 | `springboot-web-cors` | 8029 | 已完成 | 全局白名单、`@CrossOrigin` 并集、预检与凭证 |
| 30 | `springboot-mapstruct` | 8030 | 已完成 | DTO/VO/Entity：嵌套、集合、类型转换、局部更新 |
| 31 | `springboot-web-i18n` | 8031 | 已完成 | 错误响应返回 i18n key；MessageSource 按请求语言对照 |

其余域 01（1–22）多为**骨架占位**，见 [`docs/索引.md`](docs/索引.md)。

## 结构

- [`SpringBoot3-Java21-计划.md`](SpringBoot3-Java21-计划.md)：百科总清单（场景索引、选型、约 448 模块）
- [`docs/进度.md`](docs/进度.md)：实现进度（优先看）
- [`docs/索引.md`](docs/索引.md)：模块文档速链 + 完成状态
- [`docs/README.md`](docs/README.md)：文档放置约定
- `springboot-*`：独立案例（端口 = `8000 + 编号`）
  - `README.md`：八节摘要
  - `docs/01~08-*.md`：展开笔记
  - `api.http`：接口自测
- `docker/`：中间件 compose（profile 按需）

## 已完成模块怎么跑

```bash
# 公共库 + Hello
mvn -pl springboot-common,springboot-hello -am spring-boot:run -Dspring-boot.run.mainClass=com.xiesn.springboot.hello.HelloApplication

# 多模块案例（只启动 demo-web）
mvn -pl springboot-maven-multimodule/demo-web -am spring-boot:run

# 或进模块目录用 IDE 运行 *Application
# hello       → http://localhost:8002/api/hello
# java21      → http://localhost:8003/api/java21
# multimodule → http://localhost:8005/api/products
# lombok      → http://localhost:8008/api/lombok/overview
# profiles    → http://localhost:8009/api/profiles/overview
# jasypt      → http://localhost:8011/api/jasypt/secrets
# lifecycle   → http://localhost:8014/api/lifecycle/timeline
# spi         → http://localhost:8016/api/spi/overview
# docker-compose → http://localhost:8017/api/docker-compose/redis/ping（需本机 Docker）
# logback     → http://localhost:8018/api/logback/status
# log4j2      → http://localhost:8019/api/log4j2/status
# web-restful → http://localhost:8023/api/restful/overview
# validation  → http://localhost:8024/api/validation/overview
# exception   → http://localhost:8025/api/exception/overview
# jackson     → http://localhost:8026/api/jackson/overview
# interceptor → http://localhost:8027/api/interceptor/chain
# resolver   → http://localhost:8028/api/resolver/me
# cors        → http://localhost:8029/api/cors/overview
# mapstruct   → http://localhost:8030/api/mapstruct/overview
# i18n        → http://localhost:8031/api/i18n/login
```

Jasypt 教学默认主密钥：`springboot-learning-demo`（可用环境变量 `JASYPT_ENCRYPTOR_PASSWORD` 覆盖）。

## 构建

```bash
mvn -pl springboot-common,springboot-hello,springboot-java21-features,springboot-maven-multimodule,springboot-lombok-hutool,springboot-profiles,springboot-jasypt,springboot-lifecycle,springboot-spi-plugin,springboot-docker-compose,springboot-logback,springboot-log4j2,springboot-web-restful,springboot-web-validation,springboot-web-exception,springboot-web-jackson,springboot-web-interceptor,springboot-web-resolver,springboot-web-cors,springboot-mapstruct,springboot-web-i18n -am package
```






