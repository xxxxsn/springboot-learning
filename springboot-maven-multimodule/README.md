# springboot-maven-multimodule

> 编号：5 · 域 01 · **状态：已完成（2026-11-21）** · 启动端口：**8005**（demo-web）  
> 展开笔记：[docs/](docs/) · 自测：[api.http](api.http) · 总进度：[docs/进度.md](../docs/进度.md)

## 1. 适用场景

企业里把「接口契约 / 业务实现 / 启动入口」拆成多个 Maven 模块：统一版本、分层依赖、可单独发布到 Nexus 私服。

## 2. 出现频率与推荐度

- 频率：高频
- 推荐度：推荐
- 整理优先级：P0

## 3. 依赖与版本

| 子模块 | packaging | 职责 |
|---|---|---|
| `demo-bom` | pom | 只锁版本，给外部工程 import |
| `demo-api` | jar | DTO + `ProductFacade` |
| `demo-service` | jar | 门面实现 |
| `demo-web` | jar（可运行） | Boot 启动 + Controller |

本聚合父 POM 用 `dependencyManagement` 管理 `demo-api` / `demo-service` 版本；**不**再 import 子 BOM，避免循环。

## 4. 关键配置

```yaml
# demo-web/src/main/resources/application.yml
server.port: 8005
```

私服示例见 [`settings-nexus.example.xml`](settings-nexus.example.xml)；`distributionManagement` 在父 POM 中注释示例。

## 5. 核心代码要点

```
demo-web (8005)
  └─ dependsOn → demo-service
                   └─ dependsOn → demo-api
demo-bom ── 对外发布的版本清单（不参与运行时依赖图）
```

- web 只依赖 service；service 实现 api 接口
- 启动类 `scanBasePackages` 必须扫到 service 包
- 只有 `demo-web` 配置 `spring-boot-maven-plugin`

## 6. 验证步骤

```bash
mvn -pl springboot-maven-multimodule/demo-web -am spring-boot:run
```

然后执行 `api.http`，或：

- `GET http://localhost:8005/api/products`
- `GET http://localhost:8005/api/products/1`
- `GET http://localhost:8005/api/products/meta/modules`

## 怎么观察（学习用）

| 方式 | 看什么 |
|---|---|
| 接口优先 | `GET /api/products`（demo-web） |
| 结构 | api / service / web 分层与 BOM，看源码比看日志更重要 |

总指南：[docs/学习观测指南.md](../docs/学习观测指南.md)

## 7. 常见坑与排障

见 [docs/07-常见坑与排障.md](docs/07-常见坑与排障.md)。

## 8. 对照方案

见 [docs/08-对照方案.md](docs/08-对照方案.md)。
