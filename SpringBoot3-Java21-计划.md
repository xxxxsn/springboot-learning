# Spring Boot 3 + Java 21 企业技术百科（个人笔记）

> **这是什么**：面向 3 年+ Java 开发者的 **Spring Boot 企业技术百科 / 个人笔记**。把不同项目里会碰到的框架、中间件、工具、模式拆成细粒度可运行模块，方便日后按场景查阅。
> **不是什么**：不是小白入门课，也不绑定「必须 N 天学完」。整理节奏随意——10 天写 1 个或 3 年写完都可以。
> **基线**：JDK 21 + Spring Boot 3.5.x（笔记基线 3.5.16）+ Spring Cloud 2025.0.x + Spring Cloud Alibaba 2025.0.x + Maven 3.9+
> **规模**：当前清单 **448 个模块**（编号开放，只增不改语义；可持续追加到 500+）
> **命名**：`springboot-分类-功能` / `springcloud-*`，同类前缀统一，文件夹排序即归类
> **工程进度**：隔一段时间回来先看 [`docs/进度.md`](docs/进度.md)（已实现模块、端口、怎么验）

---

## 一、怎么用这份笔记

| 用法 | 怎么做 |
|---|---|
| **看实现进度** | [`docs/进度.md`](docs/进度.md) → 已完成有哪些；[`docs/索引.md`](docs/索引.md) → 状态速览 |
| **按场景查** | 先看「场景索引 / 故障速查」，跳到模块编号 |
| **按技术域逛** | 按第八节域目录浏览（Web / 数据 / Redis / MQ / 安全…） |
| **按频率学** | 优先整理标记为「高频」的模块，再补「常见 / 专项」 |
| **对照选型** | 看第五节选型表 + 模块「推荐度」列，打开对照模块对比 |
| **当培训材料** | 附录 A/B/C 有建议顺序、竖切里程碑、综合项目（可忽略） |

### 已落地代码（截至 2026-11-21）

| # | 模块 | 端口 | 说明 |
|---|---|---|---|
| 1 | `springboot-common` | — | 统一 R、异常、分页、工具 |
| 2 | `springboot-hello` | 8002 | Boot3 起步 + 自定义自动配置 |
| 3 | `springboot-java21-features` | 8003 | Java 21 语言特性演示 |
| 5 | `springboot-maven-multimodule` | 8005 | 多模块 BOM + api/service/web 分层 |
| 8 | `springboot-lombok-hutool` | 8008 | Lombok 坑 + Hutool / Guava |
| 9 | `springboot-profiles` | 8009 | 多环境、配置优先级、config.import |
| 11 | `springboot-jasypt` | 8011 | 配置 ENC 加解密 |
| 14 | `springboot-lifecycle` | 8014 | 启动/停机时间线 + 优雅停机 |
| 16 | `springboot-spi-plugin` | 8016 | Java/Spring SPI、插件化 |
| 17 | `springboot-docker-compose` | 8017 | spring-boot-docker-compose + Redis |
| 18 | `springboot-logback` | 8018 | MDC、JSON、分文件 |
| 19 | `springboot-log4j2` | 8019 | Log4j2 异步对比 |
| 23 | `springboot-web-restful` | 8023 | REST 规范、统一 R、HTTP 状态对照 |
| 24 | `springboot-web-validation` | 8024 | 自定义注解、分组、嵌套、手动 Validator |
| 31 | `springboot-web-i18n` | 8031 | 错误码返回 i18n key，MessageSource 对照 |

其余仍以本清单为索引；域 01 其它模块目前多为工程骨架。

**为什么拆这么细**：企业项目里同一个问题往往有多种解法。细拆后，遇到「订单超时取消」就能直接打开延迟队列 / Rabbit 延迟 / 时间轮等对照模块，而不是翻一个大而全的 Demo。

---

## 二、版本基线与 Boot 2→3 坑

### 2.1 版本

| 版本线 | 说明 |
|---|---|
| Spring Boot 3.5.x | 本期笔记基线（国内生态与存量项目仍以 3.x 为主） |
| Spring Boot 4.x | 单独模块记录迁移差异（Jackson 3、模块化 starter 等） |
| Spring Cloud 2025.0.x | 对应 Boot 3.5；SCA 选对应 2025.0.x |

### 2.2 Boot 2 → 3 必记坑

1. `javax.*` → `jakarta.*`
2. `spring.factories` → `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
3. `spring.redis.*` → `spring.data.redis.*`
4. Security 6：去掉 `WebSecurityConfigurerAdapter` → `SecurityFilterChain` + Lambda DSL
5. MySQL 驱动：`mysql:mysql-connector-java` → `com.mysql:mysql-connector-j`
6. 编译加 `-parameters`
7. 第三方 starter 换 Boot 3 坐标（如 `mybatis-plus-spring-boot3-starter`）

### 2.3 旧工程盘点摘要（E:\Project\springboot-learning）

根 pom 缺失目录补齐；Java 8/Boot 2.5 → 21/3.5；fastjson1 → Jackson/fastjson2；废弃依赖（springfox、RHLC、旧 OAuth2、Atomikos、Ehcache2、EasyExcel 停更等）替换；重复模块合并；拼写与垃圾文件清理。

---

## 三、模块笔记模板（每个模块 README 统一）

整理代码时按此模板写，保证「详细可查阅」：

```markdown
# springboot-xxx

## 1. 适用场景（什么项目会用到）
## 2. 出现频率（高频/常见/专项）与推荐度（推荐/对照/了解）
## 3. 依赖与版本
## 4. 关键配置
## 5. 核心代码要点（含为何这样写）
## 6. 验证步骤（配合 api.http）
## 7. 常见坑与排障
## 8. 对照方案（链到其他模块编号）
```

清单里每条至少有：一句话核心内容 + 频率 + 推荐度 + 整理优先级。代码可按自己节奏慢慢补。

---

## 四、工程约定

```
springboot-learning/
├── SpringBoot3-Java21-教学计划.md   # 百科总清单（本文件）
├── pom.xml
├── docker/docker-compose.yml       # profile 按需启动中间件
├── sql/
├── docs/                           # 文档约定 + 模块索引
│   ├── README.md
│   └── 索引.md
├── springboot-common/              # R、异常、BaseEntity、分页、工具
├── springboot-xxx/
│   ├── README.md                   # 八节摘要入口
│   ├── docs/                       # 01~08 展开笔记（可先空）
│   ├── api.http
│   └── src/...
└── springcloud-demo/               # gateway / user / order / product / account
```

- 模块只依赖 `springboot-common` + 本技术依赖，互相独立可启动
- 端口：`8000 + 序号`
- 业务主线便于串联：**用户 + 商品 + 订单 + 支付**
- 编号开放：新增模块继续往后编号，不回收旧号

**父 POM 骨架**

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.5.16</version>
</parent>
<properties>
    <java.version>21</java.version>
    <maven.compiler.parameters>true</maven.compiler.parameters>
    <spring-cloud.version>2025.0.0</spring-cloud.version>
    <spring-cloud-alibaba.version>2025.0.0.0</spring-cloud-alibaba.version>
</properties>
```

---

## 五、标签说明

### 5.1 出现频率

| 标签 | 含义 |
|---|---|
| **高频** | 多数后台/中台项目都会碰到 |
| **常见** | 中大型或特定业务线常见 |
| **专项** | 行业/信创/物联网/数仓等专项 |

### 5.2 推荐度

| 标签 | 含义 |
|---|---|
| **推荐** | 国内落地优先路径 |
| **对照** | 另一主流方案，必须保留案例 |
| **了解** | 遗留、加分或窄场景 |

### 5.3 整理优先级（原 P0/P1/P2，不绑定上课天数）

| 级别 | 含义 |
|---|---|
| **P0** | 优先写透：原理 + 完整可运行 + 坑 |
| **P1** | 其次：可运行 + README 完整 |
| **P2** | 有空再补：先保证 README 骨架与依赖坐标 |

---

## 六、选型对照表

| 领域 | 推荐 | 对照 | 了解 |
|---|---|---|---|
| HTTP 客户端 | RestClient + `@HttpExchange` | Forest | Retrofit / OkHttp 裸调 |
| ORM | MyBatis-Plus | MyBatis / JPA / jOOQ | MyBatis-Flex / Fluent |
| 连接池 | HikariCP | Druid | — |
| 缓存 | Spring Cache + Redis + Redisson | Caffeine / 两级缓存 | JetCache / Ehcache |
| MQ | RabbitMQ（业务） | Kafka（流）/ RocketMQ（交易） | Pulsar / ActiveMQ / MQTT |
| 可靠消息 | 本地消息表 + Outbox | 事务消息 | Inbox / 最大努力通知 |
| 调度 | XXL-JOB | Quartz / ShedLock / ElasticJob | PowerJob / SnailJob |
| 鉴权 | Sa-Token | Security + JWT | OAuth2 AS / Keycloak / SSO |
| 配置中心 | Nacos | Apollo | Consul KV |
| 网关 | Spring Cloud Gateway | APISIX / Higress | Kong / OpenResty |
| 流控 | Sentinel | Resilience4j / Bucket4j | Guava RateLimiter |
| 文档 | Knife4j | springdoc | smart-doc |
| 文件 | MinIO | x-file-storage / 云 OSS | FTP |
| Excel | FastExcel | POI / EasyPOI | — |
| 链路追踪 | SkyWalking | OTel / Zipkin | Pinpoint |
| DB 迁移 | Flyway | Liquibase | — |
| 工作流 | Flowable | Warm-Flow | Camunda / Activiti |
| 报表 | JimuReport | UReport2 | Jasper |
| RPC | OpenFeign | Dubbo / gRPC | — |

---

## 七、场景索引与故障速查

### 7.1 场景 → 模块

| 场景 | 模块 # |
|---|---|
| 统一返回 / 全局异常 / ProblemDetail | 1, 23, 25 |
| 参数校验 / Long 精度 / 日期序列化 | 24, 26 |
| 自定义当前用户 / 跨域 | 28, 29 |
| 操作日志 / 登录日志 / 字段审计 | 50, 51, 52, 389 |
| 数据字典 / 树形 / 单据编号 / 短链 | 249–252 |
| 事务失效 / 库存扣减 / 死锁 | 67, 68 |
| 深分页 / 慢 SQL / 千万表 | 69 |
| 读写分离 / 多租户 / 数据权限 | 72–74, 342 |
| 分库分表 / Proxy 层 | 76, 326–327 |
| 信创库 / 云数据库适配 | 78, 79, 332–335 |
| Canal / CDC / Flink CDC / DataX | 80, 81, 300–303 |
| 分布式 ID / Leaf / CosId | 82, 343, 344 |
| Seata / TCC / Saga / Outbox / Inbox | 83, 84, 199, 290–294 |
| 缓存穿透击穿雪崩 | 92, 98 |
| 分布式锁 / 限流 / 延迟队列 / 秒杀 | 96, 97, 99, 106, 310–311 |
| ES / OpenSearch / 同步方案 | 109–113, 336, 353 |
| Rabbit / Kafka / RocketMQ / 选型 | 119–129 |
| 延时消息横评 | 295 |
| 事件 / 异步 / TTL 上下文 | 130–135 |
| XXL-JOB / ShedLock / ElasticJob | 136–143, 275 |
| Sa-Token / Security / 开放平台 | 146–164 |
| 密码哈希 / TOTP / WebAuthn / mTLS | 368–371 |
| 等保 / 日志脱敏 / 审计导出 | 372–374, 389 |
| 分片上传 / MinIO / 预览 / 水印 | 165–166, 261–263 |
| 百万 Excel 异步导出 | 170, 171, 253 |
| 钉钉 / 飞书 / 企微 | 180–183, 270–273 |
| 微信/支付宝/银联/聚合支付 | 184–186, 278–280 |
| 电子签章 / OFD / 电子发票 | 281–283 |
| 报表 / 轻量工作流 | 276, 277 |
| Nacos / Apollo / Feign / Gateway | 189–195, 274 |
| gRPC / Protobuf | 305–307 |
| APISIX / Kong / Higress / OpenResty | 308–309, 439–440 |
| Sentinel / 特征开关 / 流量染色 | 196–197, 313–316 |
| SkyWalking / OTel / Loki | 202–203, 356–357 |
| Arthas / JFR / MAT / 压测 | 217–220, 358–361, 382 |
| 漏洞扫描 / SBOM / Chaos | 362–366 |
| Docker / K8s / CI/CD / 优雅停机 | 221–227 |
| Spring AI / RAG / MCP / Milvus | 232–236, 339 |
| DDD / Modulith / 策略责任链 | 239–241, 420–424, 433 |
| BFF / 接口废弃 / client stub | 317, 425–427, 435 |

### 7.2 故障速查

| 现象 | 先看 |
|---|---|
| 重复提交 / 重复扣款 | 163, 209, 123, 290–292 |
| 缓存与 DB 不一致 | 92, 80, 300 |
| 锁了仍超卖 | 96, 68, 106 |
| 线程池丢 traceId / 用户 | 135, 18 |
| Feign 超时雪崩 | 191, 192, 196, 297 |
| 网关 401 / 跨域 | 194, 195, 206 |
| 线上 CPU 高 / 变慢 | 217, 218, 220, 358 |
| 发布中断流 | 227, 14 |
| OOM / 泄漏 | 218, 217, 360 |
| 配置热更新不生效 | 190, 274, 419 |
| 读写分离读到旧数据 | 72, 325 |

---

## 八、模块总清单（420）

图例：**来源** 迁/重/新 · **级别** P0/P1/P2 · **频率** 高频/常见/专项 · **推荐度** 推荐/对照/了解

---

### 域 01 · 工程基础与 Boot 核心（1–22）

| # | 模块名 | 核心内容 | 来源 | 级别 | 频率 | 推荐度 |
|---|---|---|---|---|---|---|
| 1 | springboot-common | 统一 R、错误码、BaseEntity、分页、工具 | 新 | P0 | 高频 | 推荐 |
| 2 | springboot-hello | Boot 3 起步、自动配置、starter、启动流程 | 新 | P0 | 高频 | 推荐 |
| 3 | springboot-java21-features | record、密封类、模式匹配、文本块、Sequenced | 新 | P1 | 高频 | 推荐 |
| 4 | springboot-virtual-threads | 虚拟线程、pinning、与平台线程压测 | 新 | P0 | 高频 | 推荐 |
| 5 | springboot-maven-multimodule | 多模块、BOM、dependencyManagement、Nexus | 新 | P0 | 高频 | 推荐 |
| 6 | springboot-gradle | Gradle Kotlin DSL | 新 | P2 | 常见 | 对照 |
| 7 | springboot-code-quality | Spotless、Checkstyle、阿里规约、Sonar | 新 | P1 | 高频 | 推荐 |
| 8 | springboot-lombok-hutool | Lombok 坑、Hutool / Guava | 新 | P1 | 高频 | 推荐 |
| 9 | springboot-profiles | 多环境、配置优先级、`spring.config.import` | 迁 | P0 | 高频 | 推荐 |
| 10 | springboot-config-properties | `@ConfigurationProperties` + 校验 + record | 新 | P0 | 高频 | 推荐 |
| 11 | springboot-jasypt | 配置敏感信息加密 | 新 | P1 | 高频 | 推荐 |
| 12 | springboot-ssl-bundle | SSL Bundle、HTTPS、双向证书 | 新 | P2 | 常见 | 了解 |
| 13 | springboot-devtools | 热部署 | 迁 | P2 | 常见 | 了解 |
| 14 | springboot-lifecycle | Runner、事件、Bean 生命周期、优雅停机 | 新 | P1 | 高频 | 推荐 |
| 15 | springboot-bean-extension | BPP、FactoryBean、ImportRegistrar、Aware | 新 | P1 | 常见 | 对照 |
| 16 | springboot-spi-plugin | Java/Spring SPI、插件化 | 新 | P2 | 常见 | 了解 |
| 17 | springboot-docker-compose | `spring-boot-docker-compose` | 新 | P1 | 常见 | 推荐 |
| 18 | springboot-logback | MDC traceId、JSON 日志、分文件 | 新 | P0 | 高频 | 推荐 |
| 19 | springboot-log4j2 | Log4j2 异步对比 | 新 | P2 | 常见 | 对照 |
| 20 | springboot-test | JUnit5、Mockito、MockMvc、`@MockitoBean` | 迁 | P0 | 高频 | 推荐 |
| 21 | springboot-testcontainers | Testcontainers + `@ServiceConnection` | 新 | P1 | 高频 | 推荐 |
| 22 | springboot-test-coverage | JaCoCo、ArchUnit | 新 | P2 | 常见 | 了解 |

---

### 域 02 · Web 与接口（23–52）

| # | 模块名 | 核心内容 | 来源 | 级别 | 频率 | 推荐度 |
|---|---|---|---|---|---|---|
| 23 | springboot-web-restful | REST 规范、统一响应 | 新 | P0 | 高频 | 推荐 |
| 24 | springboot-web-validation | 参数校验、分组、自定义注解 | 新 | P0 | 高频 | 推荐 |
| 25 | springboot-web-exception | 全局异常 + ProblemDetail | 新 | P0 | 高频 | 推荐 |
| 26 | springboot-web-jackson | 日期、Long 精度、枚举、空值 | 新 | P0 | 高频 | 推荐 |
| 27 | springboot-web-interceptor | Filter / Interceptor / Listener | 新 | P1 | 高频 | 推荐 |
| 28 | springboot-web-resolver | `@CurrentUser`、返回值处理器 | 新 | P1 | 高频 | 推荐 |
| 29 | springboot-web-cors | 跨域 | 新 | P1 | 高频 | 推荐 |
| 30 | springboot-mapstruct | DTO/VO/Entity 转换 | 新 | P1 | 高频 | 推荐 |
| 31 | springboot-web-i18n | 错误码返回 i18n key，前端文案对照；MessageSource 只作服务端翻译 | 重 | P0 | 高频 | 推荐 |
| 32 | springboot-web-contentnegotiation | 内容协商 | 迁 | P2 | 专项 | 了解 |
| 33 | springboot-web-functional | RouterFunction | 迁 | P2 | 专项 | 对照 |
| 34 | springboot-web-container | Tomcat/Undertow/Jetty、HTTP/2 | 新 | P2 | 常见 | 了解 |
| 35 | springboot-web-thymeleaf | 服务端模板 | 新 | P2 | 常见 | 了解 |
| 36 | springboot-web-restclient | RestClient + `@HttpExchange` | 新 | P0 | 高频 | 推荐 |
| 37 | springboot-http-forest | Forest | 新 | P2 | 常见 | 对照 |
| 38 | springboot-http-retrofit | Retrofit starter | 新 | P2 | 专项 | 了解 |
| 39 | springboot-web-sse | SseEmitter | 新 | P1 | 常见 | 推荐 |
| 40 | springboot-web-websocket | WebSocket / STOMP | 迁 | P1 | 常见 | 推荐 |
| 41 | springboot-web-websocket-cluster | 集群 WS + Redis Pub/Sub | 新 | P1 | 常见 | 推荐 |
| 42 | springboot-web-webflux | 响应式入门 | 新 | P2 | 常见 | 了解 |
| 43 | springboot-web-graphql | Spring for GraphQL | 新 | P2 | 专项 | 了解 |
| 44 | springboot-web-api-version | API 版本控制 | 新 | P2 | 常见 | 了解 |
| 45 | springboot-doc-springdoc | springdoc-openapi | 重 | P1 | 高频 | 对照 |
| 46 | springboot-doc-knife4j | Knife4j | 迁 | P0 | 高频 | 推荐 |
| 47 | springboot-doc-smart-doc | 无注解文档 | 新 | P2 | 专项 | 了解 |
| 48 | springboot-doc-db | screw 库表文档 | 迁 | P2 | 常见 | 了解 |
| 49 | springboot-aop-basic | AOP、请求日志切面 | 迁 | P0 | 高频 | 推荐 |
| 50 | springboot-log-operation | 注解操作日志异步入库 | 迁 | P0 | 高频 | 推荐 |
| 51 | springboot-log-login | 登录日志、ip2region、在线用户 | 新 | P1 | 高频 | 推荐 |
| 52 | springboot-log-data-change | Javers 字段审计 | 新 | P2 | 常见 | 了解 |

---

### 域 03 · 数据访问与事务（53–85）

| # | 模块名 | 核心内容 | 来源 | 级别 | 频率 | 推荐度 |
|---|---|---|---|---|---|---|
| 53 | springboot-orm-jdbc-template | JdbcTemplate + JdbcClient | 迁 | P2 | 常见 | 对照 |
| 54 | springboot-orm-h2 | H2 测用 | 迁 | P2 | 常见 | 了解 |
| 55 | springboot-orm-spring-data-jdbc | Spring Data JDBC | 新 | P2 | 专项 | 了解 |
| 56 | springboot-orm-jpa | Spring Data JPA | 新 | P1 | 常见 | 对照 |
| 57 | springboot-orm-jpa-advanced | Specification、QueryDSL、N+1 | 新 | P2 | 常见 | 了解 |
| 58 | springboot-orm-mybatis | MyBatis XML/动态 SQL | 迁 | P0 | 高频 | 推荐 |
| 59 | springboot-orm-mybatis-plugin | 拦截器、慢 SQL、插件 | 新 | P1 | 高频 | 推荐 |
| 60 | springboot-orm-mybatis-generator | MBG | 迁 | P2 | 常见 | 了解 |
| 61 | springboot-orm-pagehelper | PageHelper | 新 | P2 | 常见 | 对照 |
| 62 | springboot-orm-mybatisplus | MP CRUD/条件/分页 | 迁 | P0 | 高频 | 推荐 |
| 63 | springboot-orm-mybatisplus-advanced | 填充、乐观锁、逻辑删除、批量 | 新 | P0 | 高频 | 推荐 |
| 64 | springboot-orm-mybatis-flex | MyBatis-Flex | 迁 | P2 | 专项 | 了解 |
| 65 | springboot-orm-codegen | 模板代码生成 | 新 | P1 | 高频 | 推荐 |
| 66 | springboot-db-druid | Druid 监控 | 迁 | P1 | 常见 | 对照 |
| 67 | springboot-db-transaction | 传播、隔离、失效 8 场景 | 新 | P0 | 高频 | 推荐 |
| 68 | springboot-db-lock | 悲观/乐观锁、扣库存、死锁 | 新 | P0 | 高频 | 推荐 |
| 69 | springboot-db-big-table | 深分页、索引、Explain、批量 | 新 | P0 | 高频 | 推荐 |
| 70 | springboot-db-flyway | DB 版本管理 | 新 | P1 | 高频 | 推荐 |
| 71 | springboot-db-liquibase | Liquibase | 新 | P2 | 常见 | 对照 |
| 72 | springboot-db-dynamic-datasource | 多数据源、读写分离 | 新 | P0 | 高频 | 推荐 |
| 73 | springboot-db-multi-tenant | 多租户插件 | 新 | P1 | 高频 | 推荐 |
| 74 | springboot-db-datascope | 数据权限 | 新 | P1 | 高频 | 推荐 |
| 75 | springboot-db-encrypt | 字段加解密 TypeHandler | 重 | P1 | 常见 | 推荐 |
| 76 | springboot-db-sharding-sphere | ShardingSphere-JDBC 5.x | 重 | P1 | 常见 | 推荐 |
| 77 | springboot-db-postgresql | PG JSONB/数组/全文 | 新 | P1 | 常见 | 对照 |
| 78 | springboot-db-xinchuang | 达梦/金仓/openGauss | 新 | P1 | 专项 | 推荐 |
| 79 | springboot-db-distributed | TiDB/OceanBase | 新 | P2 | 专项 | 了解 |
| 80 | springboot-db-canal | Canal → 缓存/ES | 新 | P1 | 常见 | 推荐 |
| 81 | springboot-db-debezium | Debezium CDC | 新 | P2 | 常见 | 对照 |
| 82 | springboot-distributed-id | 雪花、号段、时钟回拨 | 新 | P1 | 高频 | 推荐 |
| 83 | springboot-tx-seata | Seata AT | 重 | P1 | 常见 | 推荐 |
| 84 | springboot-tx-seata-tcc | TCC/Saga | 新 | P2 | 常见 | 对照 |
| 85 | springboot-batch | Spring Batch | 新 | P1 | 常见 | 了解 |

---

### 域 04 · 缓存与 Redis（86–106）

| # | 模块名 | 核心内容 | 来源 | 级别 | 频率 | 推荐度 |
|---|---|---|---|---|---|---|
| 86 | springboot-cache-spring-cache | `@Cacheable`、key 设计 | 新 | P0 | 高频 | 推荐 |
| 87 | springboot-cache-redis | RedisTemplate、五数据结构 | 迁 | P0 | 高频 | 推荐 |
| 88 | springboot-cache-caffeine | 本地缓存 | 迁 | P1 | 高频 | 推荐 |
| 89 | springboot-cache-ehcache | Ehcache 3 + JCache | 重 | P2 | 专项 | 了解 |
| 90 | springboot-cache-multilevel | 两级缓存 | 新 | P1 | 常见 | 推荐 |
| 91 | springboot-cache-jetcache | JetCache | 新 | P2 | 专项 | 了解 |
| 92 | springboot-cache-consistency | 穿透/击穿/雪崩、延迟双删 | 新 | P0 | 高频 | 推荐 |
| 93 | springboot-redis-cluster | 主从/哨兵/Cluster | 新 | P1 | 高频 | 推荐 |
| 94 | springboot-redis-redisson | Redisson | 新 | P0 | 高频 | 推荐 |
| 95 | springboot-redis-lua | Lua 原子操作 | 新 | P1 | 高频 | 推荐 |
| 96 | springboot-redis-distributed-lock | 分布式锁、看门狗、注解锁 | 迁 | P0 | 高频 | 推荐 |
| 97 | springboot-redis-ratelimit | 窗口/令牌桶限流 | 迁 | P0 | 高频 | 推荐 |
| 98 | springboot-redis-bloomfilter | 布隆过滤器 | 迁 | P1 | 常见 | 推荐 |
| 99 | springboot-redis-delay-queue | 延迟队列 | 新 | P1 | 高频 | 推荐 |
| 100 | springboot-redis-geo-rank | GEO + 排行榜 | 新 | P1 | 常见 | 推荐 |
| 101 | springboot-redis-bitmap-hll | 签到 Bitmap、UV HLL | 新 | P1 | 常见 | 推荐 |
| 102 | springboot-redis-feed | Feed 推拉 | 新 | P2 | 专项 | 了解 |
| 103 | springboot-redis-stream | Pub/Sub 与 Stream | 新 | P2 | 常见 | 对照 |
| 104 | springboot-redis-session | Spring Session | 新 | P1 | 常见 | 对照 |
| 105 | springboot-redis-bigkey-hotkey | 大 Key/热 Key | 新 | P2 | 常见 | 了解 |
| 106 | springboot-redis-seckill | 秒杀预扣+Lua+异步 | 迁 | P0 | 常见 | 推荐 |

---

### 域 05 · NoSQL / 搜索 / 专用存储（107–118）

| # | 模块名 | 核心内容 | 来源 | 级别 | 频率 | 推荐度 |
|---|---|---|---|---|---|---|
| 107 | springboot-nosql-mongodb | Mongo CRUD、聚合 | 迁 | P1 | 常见 | 推荐 |
| 108 | springboot-nosql-mongodb-advanced | GridFS、多文档事务 | 新 | P2 | 专项 | 了解 |
| 109 | springboot-search-es-java | ES 8 Java Client | 迁 | P0 | 高频 | 推荐 |
| 110 | springboot-search-es-data | Spring Data ES | 迁 | P1 | 常见 | 对照 |
| 111 | springboot-search-easy-es | Easy-ES | 迁 | P2 | 专项 | 了解 |
| 112 | springboot-search-es-advanced | IK/拼音/建议/高亮/聚合 | 新 | P1 | 高频 | 推荐 |
| 113 | springboot-search-es-sync | MySQL→ES 三种同步 | 新 | P1 | 高频 | 推荐 |
| 114 | springboot-olap-clickhouse | ClickHouse | 新 | P1 | 常见 | 推荐 |
| 115 | springboot-olap-doris | Doris | 新 | P2 | 专项 | 了解 |
| 116 | springboot-tsdb-tdengine | TDengine | 新 | P2 | 专项 | 了解 |
| 117 | springboot-tsdb-influxdb | InfluxDB | 新 | P2 | 专项 | 了解 |
| 118 | springboot-graph-neo4j | Neo4j | 新 | P2 | 专项 | 了解 |

---

### 域 06 · 消息队列与异步（119–135）

| # | 模块名 | 核心内容 | 来源 | 级别 | 频率 | 推荐度 |
|---|---|---|---|---|---|---|
| 119 | springboot-mq-rabbitmq | 五种模式、交换机 | 迁 | P0 | 高频 | 推荐 |
| 120 | springboot-mq-rabbitmq-advanced | 确认、死信、延迟、幂等 | 新 | P0 | 高频 | 推荐 |
| 121 | springboot-mq-kafka | 分区、消费组、offset | 迁 | P1 | 高频 | 推荐 |
| 122 | springboot-mq-kafka-advanced | 幂等、顺序、事务、EOS | 新 | P1 | 常见 | 对照 |
| 123 | springboot-mq-reliable-message | 本地消息表/事务消息 | 新 | P0 | 高频 | 推荐 |
| 124 | springboot-mq-rocketmq | RocketMQ 5 | 迁 | P1 | 常见 | 对照 |
| 125 | springboot-mq-rocketmq-advanced | 重试、死信、轨迹 | 新 | P2 | 常见 | 了解 |
| 126 | springboot-mq-activemq | ActiveMQ/Artemis | 迁 | P2 | 专项 | 了解 |
| 127 | springboot-mq-mqtt | MQTT（EMQX） | 新 | P2 | 专项 | 了解 |
| 128 | springboot-mq-pulsar | Pulsar | 新 | P2 | 专项 | 了解 |
| 129 | springboot-mq-comparison | MQ 选型横评 | 新 | P1 | 高频 | 推荐 |
| 130 | springboot-event | `@TransactionalEventListener` | 迁 | P0 | 高频 | 推荐 |
| 131 | springboot-event-spring-modulith | Modulith 模块事件 | 新 | P2 | 常见 | 对照 |
| 132 | springboot-async | `@Async`、线程池、虚拟线程 | 迁 | P0 | 高频 | 推荐 |
| 133 | springboot-async-orchestration | CompletableFuture 编排 | 重 | P1 | 高频 | 推荐 |
| 134 | springboot-async-disruptor | Disruptor | 新 | P2 | 专项 | 了解 |
| 135 | springboot-ttl-context | TTL 传 traceId/用户 | 新 | P0 | 高频 | 推荐 |

---

### 域 07 · 调度与规则（136–145）

| # | 模块名 | 核心内容 | 来源 | 级别 | 频率 | 推荐度 |
|---|---|---|---|---|---|---|
| 136 | springboot-task | `@Scheduled`、动态 Cron | 迁 | P1 | 高频 | 推荐 |
| 137 | springboot-task-quartz | Quartz 集群 | 迁 | P1 | 常见 | 对照 |
| 138 | springboot-task-xxl-job | XXL-JOB、分片广播 | 迁 | P0 | 高频 | 推荐 |
| 139 | springboot-task-xxl-job-advanced | 编排、重试、告警 | 新 | P1 | 常见 | 推荐 |
| 140 | springboot-task-shedlock | 多实例防重复 | 新 | P1 | 高频 | 推荐 |
| 141 | springboot-task-snailjob | Snail Job | 新 | P2 | 专项 | 了解 |
| 142 | springboot-task-powerjob | PowerJob | 新 | P2 | 专项 | 了解 |
| 143 | springboot-dynamic-thread-pool | Hippo4j | 新 | P1 | 常见 | 推荐 |
| 144 | springboot-rule-engine | LiteFlow/Aviator/Drools | 新 | P1 | 常见 | 推荐 |
| 145 | springboot-statemachine | 订单状态机 | 新 | P2 | 常见 | 了解 |

---

### 域 08 · 安全认证与接口安全（146–164）

| # | 模块名 | 核心内容 | 来源 | 级别 | 频率 | 推荐度 |
|---|---|---|---|---|---|---|
| 146 | springboot-security | Security 6 过滤器链 | 迁 | P0 | 高频 | 对照 |
| 147 | springboot-security-jwt | Security + JWT 双 Token | 新 | P1 | 高频 | 对照 |
| 148 | springboot-security-rbac | RBAC 动态权限 | 新 | P1 | 高频 | 推荐 |
| 149 | springboot-security-auth-server | Spring Authorization Server | 重 | P1 | 常见 | 了解 |
| 150 | springboot-security-oauth2-client | 第三方 OAuth2/SSO | 重 | P1 | 常见 | 对照 |
| 151 | springboot-security-resource-server | 资源服务器 | 重 | P2 | 常见 | 了解 |
| 152 | springboot-sa-token | Sa-Token 登录权限踢人 | 迁 | P0 | 高频 | 推荐 |
| 153 | springboot-sa-token-sso | Sa-Token SSO | 新 | P2 | 常见 | 了解 |
| 154 | springboot-sa-token-oauth2 | Sa-Token OAuth2 | 新 | P2 | 专项 | 了解 |
| 155 | springboot-justauth | JustAuth | 新 | P1 | 常见 | 推荐 |
| 156 | springboot-captcha | 图形/滑块/行为验证 | 新 | P1 | 高频 | 推荐 |
| 157 | springboot-crypto | 国密 SM2/3/4、RSA、AES | 迁 | P1 | 专项 | 推荐 |
| 158 | springboot-api-encrypt | 加解密、签名、防重放 | 重 | P0 | 高频 | 推荐 |
| 159 | springboot-sensitive | 脱敏 | 迁 | P1 | 高频 | 推荐 |
| 160 | springboot-translate | 字典翻译 | 迁 | P1 | 高频 | 推荐 |
| 161 | springboot-web-xss | XSS | 迁 | P1 | 高频 | 推荐 |
| 162 | springboot-web-csrf | CSRF | 新 | P2 | 常见 | 对照 |
| 163 | springboot-web-repeatsubmit | 防重复提交/幂等 | 迁 | P0 | 高频 | 推荐 |
| 164 | springboot-openapi | 开放平台 API | 新 | P1 | 常见 | 推荐 |

---

### 域 09 · 文件 / 办公 / 通讯 / 支付 / 流程（165–188）

| # | 模块名 | 核心内容 | 来源 | 级别 | 频率 | 推荐度 |
|---|---|---|---|---|---|---|
| 165 | springboot-file-upload-local | 分片、断点、秒传 | 迁 | P0 | 高频 | 推荐 |
| 166 | springboot-file-minio | MinIO、预签名 | 新 | P0 | 高频 | 推荐 |
| 167 | springboot-file-storage | x-file-storage 多云 | 新 | P2 | 常见 | 对照 |
| 168 | springboot-file-ftp | FTP/SFTP | 新 | P2 | 常见 | 了解 |
| 169 | springboot-excel-poi | POI 原生 | 迁 | P2 | 常见 | 对照 |
| 170 | springboot-excel-fastexcel | FastExcel | 重 | P0 | 高频 | 推荐 |
| 171 | springboot-excel-async-center | 异步导入导出中心 | 新 | P1 | 高频 | 推荐 |
| 172 | springboot-word-poitl | poi-tl Word | 新 | P2 | 常见 | 了解 |
| 173 | springboot-pdf | PDF 合同/报表 | 新 | P2 | 常见 | 了解 |
| 174 | springboot-report | Jasper/UReport2 | 新 | P2 | 常见 | 了解 |
| 175 | springboot-qrcode | ZXing | 新 | P2 | 常见 | 了解 |
| 176 | springboot-barcode | 条码 | 新 | P2 | 专项 | 了解 |
| 177 | springboot-mail | 模板邮件 | 迁 | P1 | 高频 | 推荐 |
| 178 | springboot-message-center | 统一消息中心 | 新 | P1 | 高频 | 推荐 |
| 179 | springboot-sms | SMS4J | 新 | P1 | 高频 | 推荐 |
| 180 | springboot-weixin-mp | 公众号 | 迁 | P1 | 常见 | 推荐 |
| 181 | springboot-weixin-miniapp | 小程序登录 | 新 | P1 | 高频 | 推荐 |
| 182 | springboot-weixin-open | 开放平台扫码 | 迁 | P2 | 常见 | 了解 |
| 183 | springboot-weixin-work | 企微 | 新 | P2 | 常见 | 了解 |
| 184 | springboot-weixin-pay | 微信支付 V3 | 迁 | P1 | 常见 | 推荐 |
| 185 | springboot-alipay | 支付宝 | 新 | P1 | 常见 | 推荐 |
| 186 | springboot-pay-aggregate | 聚合支付、回调幂等、对账 | 新 | P1 | 常见 | 推荐 |
| 187 | springboot-workflow-flowable | Flowable 7 | 新 | P2 | 常见 | 了解 |
| 188 | springboot-workflow-flowable-advanced | 会签/或签/驳回/转办 | 新 | P2 | 常见 | 了解 |

---

### 域 10 · 微服务与云原生（189–210）

| # | 模块名 | 核心内容 | 来源 | 级别 | 频率 | 推荐度 |
|---|---|---|---|---|---|---|
| 189 | springcloud-nacos | 注册发现 | 新 | P0 | 高频 | 推荐 |
| 190 | springcloud-nacos-config | 配置中心、热更新 | 新 | P0 | 高频 | 推荐 |
| 191 | springcloud-openfeign | Feign、超时重试 | 新 | P0 | 高频 | 推荐 |
| 192 | springcloud-openfeign-advanced | 传 Token、上传、降级 | 新 | P1 | 高频 | 推荐 |
| 193 | springcloud-loadbalancer | 负载均衡 | 新 | P1 | 高频 | 推荐 |
| 194 | springcloud-gateway | 路由、断言、过滤器 | 新 | P0 | 高频 | 推荐 |
| 195 | springcloud-gateway-advanced | 动态路由、鉴权、灰度 | 新 | P0 | 高频 | 推荐 |
| 196 | springcloud-sentinel | 流控、降级、热点 | 新 | P0 | 高频 | 推荐 |
| 197 | springcloud-sentinel-advanced | 持久化、网关/集群限流 | 新 | P1 | 常见 | 推荐 |
| 198 | springcloud-resilience4j | Resilience4j | 新 | P2 | 常见 | 对照 |
| 199 | springcloud-seata-micro | Seata AT + Nacos | 新 | P1 | 常见 | 推荐 |
| 200 | springcloud-dubbo | Dubbo 3、Triple | 新 | P1 | 常见 | 对照 |
| 201 | springcloud-dubbo-advanced | 负载、超时、泛化 | 新 | P2 | 常见 | 了解 |
| 202 | springcloud-skywalking | SkyWalking | 新 | P1 | 高频 | 推荐 |
| 203 | springcloud-micrometer-tracing | Micrometer + Zipkin | 新 | P1 | 常见 | 对照 |
| 204 | springcloud-gray | 灰度发布 | 新 | P1 | 常见 | 推荐 |
| 205 | springcloud-mesh | Service Mesh 概念 | 新 | P2 | 专项 | 了解 |
| 206 | springcloud-oauth2-micro | 网关统一鉴权 | 新 | P1 | 高频 | 推荐 |
| 207 | springcloud-contract | Spring Cloud Contract | 新 | P2 | 专项 | 了解 |
| 208 | springcloud-practice | 幂等/锁/限流/拆分实践 | 新 | P0 | 高频 | 推荐 |
| 209 | springcloud-idempotent | 幂等方案汇总 | 新 | P1 | 高频 | 推荐 |
| 210 | springcloud-canary-release | 灰度→全量→回滚 | 新 | P2 | 常见 | 了解 |

---

### 域 11 · 运维可观测与性能（211–231）

| # | 模块名 | 核心内容 | 来源 | 级别 | 频率 | 推荐度 |
|---|---|---|---|---|---|---|
| 211 | springboot-actuator | 健康检查、端点安全 | 新 | P0 | 高频 | 推荐 |
| 212 | springboot-admin | Spring Boot Admin 3 | 迁 | P1 | 常见 | 推荐 |
| 213 | springboot-observability | Prometheus + Grafana | 新 | P0 | 高频 | 推荐 |
| 214 | springboot-observability-alert | Alertmanager | 新 | P2 | 常见 | 了解 |
| 215 | springboot-log-elk | ELK/Filebeat | 新 | P1 | 高频 | 推荐 |
| 216 | springboot-log-plumelog | Plumelog | 新 | P2 | 专项 | 了解 |
| 217 | springboot-arthas | 线上诊断 | 新 | P0 | 高频 | 推荐 |
| 218 | springboot-jvm-tuning | GC、OOM、泄漏 | 新 | P0 | 高频 | 推荐 |
| 219 | springboot-jmh | JMH | 新 | P2 | 专项 | 了解 |
| 220 | springboot-jmeter | JMeter | 新 | P1 | 高频 | 推荐 |
| 221 | springboot-docker | 分层 Dockerfile、Jib | 重 | P0 | 高频 | 推荐 |
| 222 | springboot-docker-multistage | 多阶段、瘦身、非 root | 新 | P1 | 高频 | 推荐 |
| 223 | springboot-k8s | Deployment/Service/Ingress/探针 | 新 | P1 | 高频 | 推荐 |
| 224 | springboot-k8s-helm | Helm、HPA | 新 | P2 | 常见 | 了解 |
| 225 | springboot-cicd | Jenkins/GitLab CI | 新 | P1 | 高频 | 推荐 |
| 226 | springboot-nginx | 反代、负载、限流、HTTPS | 新 | P1 | 高频 | 推荐 |
| 227 | springboot-graceful-shutdown | 优雅停机与滚动发布 | 新 | P1 | 高频 | 推荐 |
| 228 | springboot-native-graalvm | GraalVM Native | 新 | P2 | 专项 | 了解 |
| 229 | springboot-package-war | 外置 Tomcat WAR | 迁 | P2 | 专项 | 了解 |
| 230 | springboot-starter-custom | 自定义 starter | 迁 | P0 | 高频 | 推荐 |
| 231 | springboot-upgrade-boot4 | 3.5→4.x 迁移 | 新 | P1 | 常见 | 推荐 |

---

### 域 12 · AI / 架构 / 综合项目壳（232–248）

| # | 模块名 | 核心内容 | 来源 | 级别 | 频率 | 推荐度 |
|---|---|---|---|---|---|---|
| 232 | springboot-ai-spring-ai | Spring AI 对话、流式 | 新 | P0 | 常见 | 推荐 |
| 233 | springboot-ai-function-calling | Function Calling | 新 | P1 | 常见 | 推荐 |
| 234 | springboot-ai-rag-pgvector | RAG（pgvector） | 新 | P1 | 常见 | 推荐 |
| 235 | springboot-ai-rag-es | RAG（ES） | 新 | P2 | 专项 | 对照 |
| 236 | springboot-ai-mcp | MCP | 新 | P1 | 常见 | 推荐 |
| 237 | springboot-ai-agent | Agent 多轮记忆 | 新 | P2 | 专项 | 了解 |
| 238 | springboot-ai-langchain4j | LangChain4j | 新 | P2 | 常见 | 对照 |
| 239 | springboot-ddd-cola | DDD/COLA | 新 | P1 | 常见 | 推荐 |
| 240 | springboot-modulith | Spring Modulith | 新 | P1 | 常见 | 推荐 |
| 241 | springboot-architecture-evolution | 架构演进路线 | 新 | P1 | 高频 | 推荐 |
| 242 | springboot-project-design | 立项：需求/表设计/选型 | 新 | P0 | 高频 | 推荐 |
| 243 | springboot-project-code | 综合项目编码壳 | 新 | P0 | 高频 | 推荐 |
| 244 | springboot-project-test | 项目测试 | 新 | P1 | 高频 | 推荐 |
| 245 | springboot-project-deploy | 项目部署监控 | 新 | P1 | 高频 | 推荐 |
| 246 | springboot-project-defend | 答辩与评审 | 新 | P2 | 专项 | 了解 |
| 247 | springboot-resume-interview | 简历与面试串讲 | 新 | P2 | 专项 | 了解 |
| 248 | springboot-certificate | 结业测评壳 | 新 | P2 | 专项 | 了解 |

---

### 域 13 · 通用业务组件（249–269）〔扩容〕

| # | 模块名 | 核心内容 | 来源 | 级别 | 频率 | 推荐度 |
|---|---|---|---|---|---|---|
| 249 | springboot-biz-dict | 数据字典（缓存+翻译） | 新 | P0 | 高频 | 推荐 |
| 250 | springboot-biz-tree | 树形：邻接表/闭包表/物化路径 | 新 | P0 | 高频 | 推荐 |
| 251 | springboot-biz-bill-no | 单据编号规则（前缀/日期/序列/租户） | 新 | P0 | 高频 | 推荐 |
| 252 | springboot-biz-shorturl | 短链接生成与跳转统计 | 新 | P1 | 常见 | 推荐 |
| 253 | springboot-biz-export-template | 导入导出模板中心 | 新 | P1 | 高频 | 推荐 |
| 254 | springboot-biz-async-task | 异步任务中心：进度/取消/结果 | 新 | P1 | 高频 | 推荐 |
| 255 | springboot-biz-inbox-read | 站内信已读回执与未读数 | 新 | P1 | 常见 | 推荐 |
| 256 | springboot-biz-region | 省市区行政区划数据 | 新 | P1 | 高频 | 推荐 |
| 257 | springboot-biz-bank-cnaps | 银行联行号基础数据 | 新 | P2 | 专项 | 了解 |
| 258 | springboot-biz-idcard-bank-usci | 身份证/银行卡/统一社会信用代码校验 | 新 | P1 | 高频 | 推荐 |
| 259 | springboot-biz-sensitive-word | 敏感词 DFA/AC 自动机 | 新 | P1 | 常见 | 推荐 |
| 260 | springboot-biz-content-safety | 内容安全云服务对接 | 新 | P2 | 常见 | 了解 |
| 261 | springboot-biz-watermark | 明水印/暗水印 | 新 | P1 | 常见 | 推荐 |
| 262 | springboot-biz-file-preview | kkFileView 等文件预览 | 新 | P1 | 常见 | 推荐 |
| 263 | springboot-biz-anti-leech | 防盗链与下载次数限制 | 新 | P1 | 常见 | 推荐 |
| 264 | springboot-biz-ip-phone | IP/手机号归属地 | 新 | P1 | 常见 | 推荐 |
| 265 | springboot-biz-dynamic-form | 动态表单/元数据驱动 CRUD | 新 | P2 | 常见 | 了解 |
| 266 | springboot-biz-codegen-full | 前后端代码生成（RuoYi 风格对照） | 新 | P1 | 常见 | 对照 |
| 267 | springboot-biz-notice-announce | 系统公告/通知已读 | 新 | P2 | 常见 | 了解 |
| 268 | springboot-biz-comment | 评论/回复通用组件 | 新 | P2 | 常见 | 了解 |
| 269 | springboot-biz-tag-category | 标签与多级分类 | 新 | P2 | 常见 | 了解 |

---

### 域 14 · 国产协作 / 报表 / 工作流 / 配置（270–289）〔扩容〕

| # | 模块名 | 核心内容 | 来源 | 级别 | 频率 | 推荐度 |
|---|---|---|---|---|---|---|
| 270 | springboot-dingtalk-login | 钉钉扫码/免登 | 新 | P1 | 高频 | 推荐 |
| 271 | springboot-dingtalk-robot | 钉钉机器人与工作通知 | 新 | P1 | 高频 | 推荐 |
| 272 | springboot-feishu-login | 飞书登录与通讯录 | 新 | P1 | 常见 | 推荐 |
| 273 | springboot-feishu-bot | 飞书机器人/审批事件 | 新 | P1 | 常见 | 推荐 |
| 274 | springboot-config-apollo | Apollo 配置中心 | 新 | P1 | 常见 | 对照 |
| 275 | springboot-task-elasticjob | ElasticJob 分片调度 | 新 | P1 | 常见 | 对照 |
| 276 | springboot-report-jimureport | 积木报表 JimuReport | 新 | P1 | 常见 | 推荐 |
| 277 | springboot-workflow-warmflow | Warm-Flow 轻量工作流 | 新 | P1 | 常见 | 对照 |
| 278 | springboot-pay-union | 银联支付对接 | 新 | P2 | 专项 | 了解 |
| 279 | springboot-pay-allinpay | 通联等聚合通道 | 新 | P2 | 专项 | 了解 |
| 280 | springboot-pay-profitsharing | 微信支付分账/合单 | 新 | P2 | 专项 | 了解 |
| 281 | springboot-invoice-electronic | 电子发票开具与红冲 | 新 | P2 | 专项 | 了解 |
| 282 | springboot-esign | 电子签章 | 新 | P2 | 专项 | 了解 |
| 283 | springboot-ofd | OFD 版式文件（信创） | 新 | P2 | 专项 | 了解 |
| 284 | springboot-magic-api | Magic-API 快速接口 | 新 | P2 | 常见 | 了解 |
| 285 | springboot-keycloak | Keycloak 集成 | 新 | P2 | 常见 | 对照 |
| 286 | springboot-ldap-ad | LDAP/AD 组织同步 | 新 | P1 | 常见 | 推荐 |
| 287 | springboot-cas-client | CAS 单点登录客户端 | 新 | P2 | 专项 | 了解 |
| 288 | springboot-push-jpush | 极光/个推消息推送 | 新 | P2 | 常见 | 了解 |
| 289 | springboot-map-amap | 高德/百度地图服务封装 | 新 | P2 | 常见 | 了解 |

---

### 域 15 · 一致性模式 / 集成 / 调度生态（290–304）〔扩容〕

| # | 模块名 | 核心内容 | 来源 | 级别 | 频率 | 推荐度 |
|---|---|---|---|---|---|---|
| 290 | springboot-pattern-outbox | Outbox 模式 | 新 | P0 | 高频 | 推荐 |
| 291 | springboot-pattern-inbox | Inbox 幂等消费 | 新 | P0 | 高频 | 推荐 |
| 292 | springboot-pattern-best-effort | 最大努力通知 | 新 | P1 | 常见 | 推荐 |
| 293 | springboot-pattern-saga-choreography | Saga 协同式 | 新 | P1 | 常见 | 对照 |
| 294 | springboot-pattern-saga-orchestration | Saga 编排式 | 新 | P1 | 常见 | 对照 |
| 295 | springboot-mq-delay-comparison | 延时消息横评（MQ/Redis/时间轮） | 新 | P1 | 高频 | 推荐 |
| 296 | springboot-cloud-stream | Spring Cloud Stream | 新 | P1 | 常见 | 对照 |
| 297 | springboot-retry | Spring Retry / Resilience4j Retry | 新 | P1 | 高频 | 推荐 |
| 298 | springboot-failsafe | Failsafe 容错库 | 新 | P2 | 专项 | 了解 |
| 299 | springboot-integration | Spring Integration | 新 | P2 | 专项 | 了解 |
| 300 | springboot-cdc-flink | Flink CDC | 新 | P1 | 常见 | 推荐 |
| 301 | springboot-etl-datax | DataX 同步任务触发与监控 | 新 | P1 | 常见 | 推荐 |
| 302 | springboot-etl-seatunnel | SeaTunnel | 新 | P2 | 常见 | 对照 |
| 303 | springboot-schedule-dolphinscheduler | DolphinScheduler 对接 | 新 | P2 | 常见 | 了解 |
| 304 | springboot-sync-otter-notes | Otter/多活同步概念笔记 | 新 | P2 | 专项 | 了解 |

---

### 域 16 · RPC / 网关 / 流量治理（305–320）〔扩容〕

| # | 模块名 | 核心内容 | 来源 | 级别 | 频率 | 推荐度 |
|---|---|---|---|---|---|---|
| 305 | springboot-rpc-grpc | gRPC 服务端/客户端 | 新 | P1 | 常见 | 推荐 |
| 306 | springboot-rpc-grpc-spring | Spring gRPC 集成 | 新 | P1 | 常见 | 推荐 |
| 307 | springboot-serialize-protobuf | Protobuf 序列化 | 新 | P1 | 常见 | 推荐 |
| 308 | springboot-gateway-apisix | APISIX 对照 Gateway | 新 | P1 | 常见 | 对照 |
| 309 | springboot-gateway-higress | Higress | 新 | P2 | 常见 | 对照 |
| 310 | springboot-ratelimit-bucket4j | Bucket4j 限流 | 新 | P1 | 常见 | 对照 |
| 311 | springboot-ratelimit-guava | Guava RateLimiter | 新 | P2 | 常见 | 了解 |
| 312 | springboot-serialize-kryo-hessian | Kryo/Hessian/MessagePack | 新 | P2 | 专项 | 了解 |
| 313 | springboot-feature-togglz | Togglz 特征开关 | 新 | P1 | 常见 | 推荐 |
| 314 | springboot-feature-ff4j | FF4J | 新 | P2 | 专项 | 对照 |
| 315 | springboot-feature-unleash | Unleash | 新 | P2 | 专项 | 对照 |
| 316 | springboot-traffic-stain | 流量染色与灰度上下文 | 新 | P1 | 常见 | 推荐 |
| 317 | springboot-bff | BFF：管理端/App/小程序适配 | 新 | P1 | 高频 | 推荐 |
| 318 | springboot-pressure-shadow | 全链路压测影子表/影子库 | 新 | P2 | 专项 | 了解 |
| 319 | springboot-http-okhttp-tune | OkHttp/HttpClient5 连接池调优 | 新 | P1 | 常见 | 推荐 |
| 320 | springboot-dns-cache | JVM DNS 缓存问题 | 新 | P2 | 常见 | 了解 |

---

### 域 17 · 数据增强 / 搜索增强（321–355）〔扩容〕

| # | 模块名 | 核心内容 | 来源 | 级别 | 频率 | 推荐度 |
|---|---|---|---|---|---|---|
| 321 | springboot-orm-jooq | jOOQ 类型安全 SQL | 新 | P1 | 常见 | 对照 |
| 322 | springboot-orm-mp-join | MyBatis-Plus 联表/多表 | 新 | P1 | 高频 | 推荐 |
| 323 | springboot-orm-fluent | Fluent MyBatis | 新 | P2 | 专项 | 了解 |
| 324 | springboot-orm-querydsl | QueryDSL 独立用法 | 新 | P2 | 常见 | 了解 |
| 325 | springboot-db-read-primary | 强制读主与一致性策略 | 新 | P1 | 高频 | 推荐 |
| 326 | springboot-db-shardingsphere-proxy | ShardingSphere-Proxy | 新 | P2 | 常见 | 对照 |
| 327 | springboot-db-proxysql | ProxySQL | 新 | P2 | 专项 | 了解 |
| 328 | springboot-db-mysql8 | MySQL 8 窗口函数/CTE/JSON | 新 | P1 | 高频 | 推荐 |
| 329 | springboot-db-mariadb | MariaDB 适配 | 新 | P2 | 专项 | 了解 |
| 330 | springboot-db-sqlserver | SQL Server 接入 | 新 | P2 | 专项 | 了解 |
| 331 | springboot-db-oracle | Oracle 接入与分页 | 新 | P2 | 专项 | 了解 |
| 332 | springboot-db-dm-advanced | 达梦进阶特性与迁移 | 新 | P2 | 专项 | 推荐 |
| 333 | springboot-db-kingbase-advanced | 人大金仓进阶 | 新 | P2 | 专项 | 推荐 |
| 334 | springboot-db-polardb | PolarDB 适配 | 新 | P2 | 专项 | 了解 |
| 335 | springboot-db-tdsql | TDSQL 适配 | 新 | P2 | 专项 | 了解 |
| 336 | springboot-search-opensearch | OpenSearch | 新 | P1 | 常见 | 对照 |
| 337 | springboot-redis-redisearch | RediSearch | 新 | P2 | 专项 | 了解 |
| 338 | springboot-redis-redisjson | RedisJSON | 新 | P2 | 专项 | 了解 |
| 339 | springboot-vector-milvus | Milvus 向量检索 | 新 | P1 | 常见 | 推荐 |
| 340 | springboot-vector-qdrant | Qdrant | 新 | P2 | 专项 | 对照 |
| 341 | springboot-search-solr | Solr（遗留对照） | 新 | P2 | 专项 | 了解 |
| 342 | springboot-db-tenant-schema | 多租户：库/Schema/字段隔离对比 | 新 | P1 | 高频 | 推荐 |
| 343 | springboot-id-leaf | 美团 Leaf 号段/雪花 | 新 | P1 | 常见 | 对照 |
| 344 | springboot-id-cosid | CosId | 新 | P2 | 专项 | 了解 |
| 345 | springboot-db-audit-plugin | 数据库审计/防水坝概念 | 新 | P2 | 专项 | 了解 |
| 346 | springboot-db-tde | 透明加密 TDE 笔记 | 新 | P2 | 专项 | 了解 |
| 347 | springboot-csv-univocity | CSV 高性能读写 | 新 | P2 | 常见 | 了解 |
| 348 | springboot-json-schema | JSON Schema 校验 | 新 | P2 | 常见 | 了解 |
| 349 | springboot-json-path | JSONPath 抽取 | 新 | P2 | 常见 | 了解 |
| 350 | springboot-xml-jackson | Jackson XML / DOM4J | 新 | P2 | 常见 | 了解 |
| 351 | springboot-db-backup-restore | 备份恢复演练清单 | 新 | P2 | 常见 | 了解 |
| 352 | springboot-olap-ch-materialized | ClickHouse 物化视图 | 新 | P2 | 专项 | 了解 |
| 353 | springboot-search-suggest | 搜索建议/同义词/纠错 | 新 | P1 | 常见 | 推荐 |
| 354 | springboot-geo-fence | 电子围栏 | 新 | P2 | 专项 | 了解 |
| 355 | springboot-track-correct | 轨迹纠偏与存储 | 新 | P2 | 专项 | 了解 |

---

### 域 18 · 可观测 / 性能 / 安全合规（356–389）〔扩容〕

| # | 模块名 | 核心内容 | 来源 | 级别 | 频率 | 推荐度 |
|---|---|---|---|---|---|---|
| 356 | springboot-otel | OpenTelemetry 全链路 | 新 | P1 | 高频 | 推荐 |
| 357 | springboot-log-loki | Loki + Grafana 日志 | 新 | P1 | 常见 | 对照 |
| 358 | springboot-profiler-async | async-profiler | 新 | P1 | 常见 | 推荐 |
| 359 | springboot-profiler-jfr | Java Flight Recorder | 新 | P1 | 常见 | 推荐 |
| 360 | springboot-heap-mat | MAT 分析堆转储 | 新 | P1 | 常见 | 推荐 |
| 361 | springboot-threaddump | 线程 dump 分析 | 新 | P1 | 高频 | 推荐 |
| 362 | springboot-chaos-blade | ChaosBlade 故障注入 | 新 | P2 | 专项 | 了解 |
| 363 | springboot-security-owasp-dep | OWASP Dependency-Check | 新 | P1 | 高频 | 推荐 |
| 364 | springboot-security-trivy | Trivy 镜像扫描 | 新 | P1 | 常见 | 推荐 |
| 365 | springboot-security-sbom | SBOM 生成与治理 | 新 | P2 | 常见 | 了解 |
| 366 | springboot-security-rasp | RASP 概念与接入 | 新 | P2 | 专项 | 了解 |
| 367 | springboot-vault-kms | Vault / 云 KMS | 新 | P1 | 常见 | 推荐 |
| 368 | springboot-mtls | 服务间 mTLS | 新 | P2 | 专项 | 了解 |
| 369 | springboot-password-hash | BCrypt/Argon2 密码存储 | 新 | P0 | 高频 | 推荐 |
| 370 | springboot-totp | TOTP 二次验证 | 新 | P1 | 常见 | 推荐 |
| 371 | springboot-webauthn | WebAuthn/Passkey | 新 | P2 | 专项 | 了解 |
| 372 | springboot-compliance-mlps | 等保技术点清单 | 新 | P1 | 专项 | 推荐 |
| 373 | springboot-log-desensitize | 日志自动脱敏 | 新 | P1 | 高频 | 推荐 |
| 374 | springboot-audit-export | 审计日志合规导出 | 新 | P1 | 常见 | 推荐 |
| 375 | springboot-privacy-minimize | 个人信息最小化与匿名化 | 新 | P2 | 专项 | 了解 |
| 376 | springboot-waf-integration | WAF 对接笔记 | 新 | P2 | 专项 | 了解 |
| 377 | springboot-agent-bytebuddy | ByteBuddy/Java Agent 入门 | 新 | P2 | 专项 | 了解 |
| 378 | springboot-metrics-custom | 自定义业务 Metrics | 新 | P1 | 高频 | 推荐 |
| 379 | springboot-slo-sli | SLO/SLI 实践笔记 | 新 | P2 | 常见 | 了解 |
| 380 | springboot-alert-noise | 告警降噪与 on-call | 新 | P2 | 常见 | 了解 |
| 381 | springboot-pinpoint | Pinpoint 对照 | 新 | P2 | 专项 | 了解 |
| 382 | springboot-gatling | Gatling 压测 | 新 | P2 | 常见 | 对照 |
| 383 | springboot-diff-release | 差异化发布结果分析 | 新 | P2 | 专项 | 了解 |
| 384 | springboot-crac | CRaC 快照恢复 | 新 | P2 | 专项 | 了解 |
| 385 | springboot-cds | CDS/AppCDS 启动优化 | 新 | P2 | 专项 | 了解 |
| 386 | springboot-structured-concurrency | Structured Concurrency | 新 | P2 | 常见 | 了解 |
| 387 | springboot-scoped-value | ScopedValue | 新 | P2 | 专项 | 了解 |
| 388 | springboot-unitization | 单元化/异地多活概念 | 新 | P2 | 专项 | 了解 |
| 389 | springboot-audit-compliance-log | 操作审计合规字段规范 | 新 | P1 | 高频 | 推荐 |

---

### 域 19 · 响应式 / 测试 / 工程化（390–419）〔扩容〕

| # | 模块名 | 核心内容 | 来源 | 级别 | 频率 | 推荐度 |
|---|---|---|---|---|---|---|
| 390 | springboot-r2dbc | R2DBC | 新 | P2 | 专项 | 了解 |
| 391 | springboot-reactive-redis | Reactive Redis | 新 | P2 | 专项 | 了解 |
| 392 | springboot-reactive-mongo | Reactive Mongo | 新 | P2 | 专项 | 了解 |
| 393 | springboot-rsocket | RSocket | 新 | P2 | 专项 | 了解 |
| 394 | springboot-kotlin | Kotlin + Spring Boot 对照 | 新 | P2 | 专项 | 了解 |
| 395 | springboot-test-wiremock | WireMock | 新 | P1 | 高频 | 推荐 |
| 396 | springboot-test-mockserver | MockServer | 新 | P2 | 常见 | 对照 |
| 397 | springboot-test-awaitility | Awaitility | 新 | P1 | 常见 | 推荐 |
| 398 | springboot-test-assertj | AssertJ | 新 | P1 | 高频 | 推荐 |
| 399 | springboot-test-pact | Pact 契约测试 | 新 | P2 | 专项 | 了解 |
| 400 | springboot-test-rest-docs | Spring REST Docs | 新 | P2 | 常见 | 了解 |
| 401 | springboot-openapi-generator | OpenAPI Generator 出 SDK | 新 | P1 | 常见 | 推荐 |
| 402 | springboot-shell | Spring Shell 运维小工具 | 新 | P2 | 常见 | 了解 |
| 403 | springboot-bom-governance | 二方库 BOM 版本治理 | 新 | P1 | 高频 | 推荐 |
| 404 | springboot-adr | ADR 架构决策记录模板 | 新 | P1 | 常见 | 推荐 |
| 405 | springboot-git-workflow | Git Flow / Trunk Based | 新 | P2 | 高频 | 了解 |
| 406 | springboot-code-review-checklist | Code Review 清单 | 新 | P2 | 高频 | 了解 |
| 407 | springboot-cloud-function | Spring Cloud Function | 新 | P2 | 专项 | 了解 |
| 408 | springboot-dapr-notes | Dapr 概念对照 | 新 | P2 | 专项 | 了解 |
| 409 | springboot-easypoi | EasyPOI | 新 | P2 | 常见 | 对照 |
| 410 | springboot-image-thumbnailator | Thumbnailator 缩略图/水印 | 新 | P1 | 常见 | 推荐 |
| 411 | springboot-ffmpeg | FFmpeg 转码/截帧对接 | 新 | P2 | 专项 | 了解 |
| 412 | springboot-netty-tcp | Netty TCP 自定义协议 | 新 | P2 | 专项 | 了解 |
| 413 | springboot-mqtt-device | 设备 MQTT 接入进阶 | 新 | P2 | 专项 | 了解 |
| 414 | springboot-qlexpress | QLExpress 规则 | 新 | P2 | 常见 | 对照 |
| 415 | springboot-pf4j | PF4J 插件化 | 新 | P2 | 专项 | 了解 |
| 416 | springboot-license | 软件 License 校验 | 新 | P2 | 专项 | 了解 |
| 417 | springboot-saas-metering | SaaS 计量计费雏形 | 新 | P2 | 专项 | 了解 |
| 418 | springboot-env-postprocessor | EnvironmentPostProcessor | 新 | P2 | 常见 | 了解 |
| 419 | springboot-refresh-scope | `@RefreshScope` 原理与坑 | 新 | P1 | 高频 | 推荐 |

---

### 域 20 · 领域模式与接口治理（420–448）〔扩容〕

| # | 模块名 | 核心内容 | 来源 | 级别 | 频率 | 推荐度 |
|---|---|---|---|---|---|---|
| 420 | springboot-pattern-strategy | 策略模式：订单优惠落地 | 新 | P1 | 高频 | 推荐 |
| 421 | springboot-pattern-chain | 责任链：审批/校验管道 | 新 | P1 | 高频 | 推荐 |
| 422 | springboot-pattern-specification | 规约模式组合查询 | 新 | P1 | 常见 | 推荐 |
| 423 | springboot-pattern-acl | 防腐层 ACL 对接外部系统 | 新 | P1 | 常见 | 推荐 |
| 424 | springboot-pattern-template-export | 模板方法：统一导出流程 | 新 | P1 | 常见 | 推荐 |
| 425 | springboot-api-client-stub | API 模块与 Feign client stub 分离 | 新 | P0 | 高频 | 推荐 |
| 426 | springboot-api-deprecation | 接口废弃与兼容策略 | 新 | P1 | 高频 | 推荐 |
| 427 | springboot-api-error-code | 企业级错误码规范 | 新 | P0 | 高频 | 推荐 |
| 428 | springboot-hexagonal | 六边形/端口适配器示例 | 新 | P2 | 常见 | 了解 |
| 429 | springboot-clean-architecture | 整洁架构分层示例 | 新 | P2 | 常见 | 了解 |
| 430 | springboot-axon-notes | Axon/CQRS 概念对照 | 新 | P2 | 专项 | 了解 |
| 431 | springboot-temporal-notes | Temporal/Cadence 工作流引擎对照 | 新 | P2 | 专项 | 了解 |
| 432 | springboot-package-structure | 企业包结构与模块边界规范 | 新 | P1 | 高频 | 推荐 |
| 433 | springboot-cola-extension | COLA 扩展点实战 | 新 | P1 | 常见 | 推荐 |
| 434 | springboot-dto-compat | DTO 字段兼容与滚动升级 | 新 | P1 | 常见 | 推荐 |
| 435 | springboot-idempotent-token | 幂等 Token 表设计模板 | 新 | P0 | 高频 | 推荐 |
| 436 | springboot-distributed-lock-zk | Curator/ZK 锁对照 | 新 | P2 | 专项 | 对照 |
| 437 | springboot-config-consul | Consul 注册与 KV | 新 | P2 | 专项 | 对照 |
| 438 | springboot-registry-eureka | Eureka 遗留对照 | 新 | P2 | 专项 | 了解 |
| 439 | springboot-gateway-kong | Kong 网关对照 | 新 | P2 | 专项 | 对照 |
| 440 | springboot-gateway-openresty | OpenResty/Nginx Lua | 新 | P2 | 常见 | 了解 |
| 441 | springboot-excel-template-fill | Excel 模板填充 | 新 | P1 | 常见 | 推荐 |
| 442 | springboot-word-bookmark | Word 书签套打 | 新 | P2 | 常见 | 了解 |
| 443 | springboot-pdf-sign | PDF 签章 | 新 | P2 | 专项 | 了解 |
| 444 | springboot-office-onlyoffice | OnlyOffice 在线编辑集成 | 新 | P2 | 专项 | 了解 |
| 445 | springboot-im-netty | 简易 IM（Netty）笔记 | 新 | P2 | 专项 | 了解 |
| 446 | springboot-websocket-socketio | Socket.IO 对照 | 新 | P2 | 专项 | 了解 |
| 447 | springboot-abtest | 简易 A/B 实验分流 | 新 | P2 | 常见 | 了解 |
| 448 | springboot-event-tracking | 服务端埋点收集 | 新 | P2 | 常见 | 了解 |

**本轮清单合计：448 个模块**（1–248 原百科 + 249–448 扩容）。编号开放，后续只追加不改旧号语义。

---

## 九、技术域速览（目录）

| 域 | 编号范围 | 主题 |
|---|---|---|
| 01 | 1–22 | 工程基础与 Boot 核心 |
| 02 | 23–52 | Web 与接口 |
| 03 | 53–85 | 数据访问与事务 |
| 04 | 86–106 | 缓存与 Redis |
| 05 | 107–118 | NoSQL / 搜索 / 专用存储 |
| 06 | 119–135 | 消息队列与异步 |
| 07 | 136–145 | 调度与规则 |
| 08 | 146–164 | 安全认证 |
| 09 | 165–188 | 文件办公通讯支付流程 |
| 10 | 189–210 | 微服务 |
| 11 | 211–231 | 运维可观测 |
| 12 | 232–248 | AI / 架构 / 项目壳 |
| 13 | 249–269 | 通用业务组件 |
| 14 | 270–289 | 国产协作 / 报表 / 配置 |
| 15 | 290–304 | 一致性模式 / ETL |
| 16 | 305–320 | RPC / 网关 / 流量 |
| 17 | 321–355 | 数据与搜索增强 |
| 18 | 356–389 | 可观测 / 合规 / 现代 Java |
| 19 | 390–419 | 响应式 / 测试 / 工程化 |
| 20 | 420–448 | 领域模式与接口治理 |

---

## 附录 A · 可选学习路径（不绑定天数）

若用于自学或开班，可按域顺序推进；**不设截止日期**。建议优先顺序：

1. 域 01 → 02 → 03 → 04（单体核心）
2. 域 08 鉴权尽早接入（可与域 02 穿插）
3. 域 06 → 07 → 13（异步、调度、业务组件）
4. 域 10 → 11（微服务与上线）
5. 域 15 → 16 → 17 → 18（模式、治理、增强、合规）
6. 域 12 / 14 / 19 / 20 按项目需要插拔

高频 + P0 模块可作「第一遍」；其余当查阅库。

---

## 附录 B · 可选竖切里程碑

| 里程碑 | 串什么 | 验收 |
|---|---|---|
| M1 单体骨架 | 1,23–26,46,50,152,249 | 可登录鉴权的用户 CRUD |
| M2 库存闭环 | 92,96,97,106 | 压测不超卖 |
| M3 下单异步 | 120,123,290,291 | 超时取消 + 最终一致 |
| M4 安全加固 | 158,163,373,435 | 签名/幂等/脱敏 |
| M5 业务集成 | 166,171,178,186 | 上传/导出/支付回调 |
| M6 微服务链路 | 189–196,206 | 五服务下单通 |
| M7 可观测上线 | 211,213,217,221,227 | 能定位一次故障 |
| M8 选型落地 | 242–245 + 自选百科 | 选型说明 + 可运行 |

---

## 附录 C · 若当培训班使用

**综合项目选题**：校园二手 / 在线教育 / 电商后台 / 智慧园区

- 必选：约 20 个高频 P0（规范、MP、Redis 锁限流、可靠消息或 Outbox、Sa-Token、防重、Knife4j、MinIO、FastExcel、日志、Actuator、Docker）
- 自选：≥10 个百科模块，书面说明为何选
- 评分建议：功能 25% / 规范 15% / 必选落地 20% / **选型说明 15%** / 部署可观测 15% / 表达 10%

**备课提示**：滚动整理；中间件 docker profile 按需启动；课堂/笔记话术固定「场景 → 方案 → 代码 → 坑/对照」。

---

## 附录 D · 持续追加约定

1. 新模块从 **449** 起编号，写入对应技术域（可新开域 21+）
2. 同步更新：场景索引、故障速查、选型表（若引入新「推荐」）
3. 每个新模块先写 README 八节模板，再补代码
4. 不删除旧模块；废弃技术改推荐度为「了解」并在核心内容注明「遗留」

---

*文档版本：个人百科笔记版 · 448 模块 · 编号开放可持续追加 · 时间表非约束*
