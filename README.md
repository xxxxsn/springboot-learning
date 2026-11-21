# springboot-learning

Spring Boot 3 + Java 21 企业技术百科（可运行案例库）。

## 先看进度

隔一段时间回来，**先打开** → [`docs/进度.md`](docs/进度.md)

当前已实现：

| # | 模块 | 端口 | 状态 |
|---|---|---|---|
| 1 | `springboot-common` | — | 已完成 |
| 2 | `springboot-hello` | 8002 | 已完成 |
| 3 | `springboot-java21-features` | 8003 | 已完成 |
| 5 | `springboot-maven-multimodule` | 8005 | 已完成 |
| 8 | `springboot-lombok-hutool` | 8008 | 已完成 |
| 9 | `springboot-profiles` | 8009 | 已完成 |
| 11 | `springboot-jasypt` | 8011 | 已完成 |
| 14 | `springboot-lifecycle` | 8014 | 已完成 |
| 16 | `springboot-spi-plugin` | 8016 | 已完成 |
| 17 | `springboot-docker-compose` | 8017 | 已完成 |
| 18 | `springboot-logback` | 8018 | 已完成 |
| 19 | `springboot-log4j2` | 8019 | 已完成 |
| 23 | `springboot-web-restful` | 8023 | 已完成 |
| 24 | `springboot-web-validation` | 8024 | 已完成 |
| 31 | `springboot-web-i18n` | 8031 | 已完成 |

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
# i18n        → http://localhost:8031/api/i18n/login
```

Jasypt 教学默认主密钥：`springboot-learning-demo`（可用环境变量 `JASYPT_ENCRYPTOR_PASSWORD` 覆盖）。

## 构建

```bash
mvn -pl springboot-common,springboot-hello,springboot-java21-features,springboot-maven-multimodule,springboot-lombok-hutool,springboot-profiles,springboot-jasypt,springboot-lifecycle,springboot-spi-plugin,springboot-docker-compose,springboot-logback,springboot-log4j2,springboot-web-restful,springboot-web-validation,springboot-web-i18n -am package
```






