# springboot-docker-compose

> 编号：17 · 域 01 · **状态：已完成（2026-11-21）** · 端口：**8017**  
> 展开笔记：[docs/](docs/) · 自测：[api.http](api.http) · 总进度：[docs/进度.md](../docs/进度.md)

## 1. 适用场景

本地开发不想先手动 `docker compose up`：引入 `spring-boot-docker-compose` 后，应用启动自动拉起 `compose.yaml` 里的中间件，并用 **Service Connection** 注入连接信息（本例为 Redis）。

## 2. 出现频率与推荐度

- 频率：常见
- 推荐度：推荐
- 整理优先级：P1

## 3. 依赖与版本

- `spring-boot-docker-compose`（optional）
- `spring-boot-starter-data-redis`
- 本机需 `docker` + `docker compose`（已验证 Compose v2+）
- 仓库根 [`docker/docker-compose.yml`](../docker/docker-compose.yml) 是「多模块共享中间件」；本模块演示的是 **Boot 内置 Compose 支持**

## 4. 关键配置

见模块根 [`compose.yaml`](compose.yaml) 与 [`application.yml`](src/main/resources/application.yml)：

- `spring.docker.compose.file=compose.yaml`
- `lifecycle-management=start-and-stop`
- 测试：`spring.docker.compose.skip.in-tests=false`（需要本机 Docker）

## 5. 核心代码要点

| 项 | 说明 |
|---|---|
| `compose.yaml` | `redis:7.4` 建连；`sidecar` + `org.springframework.boot.ignore` 不建连 |
| Service Connection | 按镜像名匹配 `DataRedisConnectionDetails`，覆盖手写 host/port |
| `RedisDemoService` | ping / SET-GET 验证真连上容器 |

## 6. 验证步骤

```bash
mvn -pl springboot-docker-compose -am spring-boot:run
```

IDE 直接跑 `DockerComposeApplication` 也可以：`ComposeFileEnvironmentPostProcessor` 会把路径解析成绝对路径（覆盖 yml 相对路径；仓库根 / 模块根 cwd 均可）。

然后访问 `GET /api/docker-compose/redis/ping`。

## 怎么观察（学习用）

| 方式 | 看什么 |
|---|---|
| 混合 | `GET /api/docker-compose/redis/ping` |
| 日志/Docker | Boot 拉起 compose 的日志；`docker compose ps` |

总指南：[docs/学习观测指南.md](../docs/学习观测指南.md)

## 7. 常见坑与排障

见 [docs/07-常见坑与排障.md](docs/07-常见坑与排障.md)。

## 8. 对照方案

见 [docs/08-对照方案.md](docs/08-对照方案.md)。
