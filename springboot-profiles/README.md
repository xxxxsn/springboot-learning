# springboot-profiles

> 编号：9 · 域 01 · **状态：已完成（2026-11-21）** · 端口：**8009**  
> 展开笔记：[docs/](docs/) · 自测：[api.http](api.http) · 总进度：[docs/进度.md](../docs/进度.md)

## 1. 适用场景

多环境（dev/test/production）切换、Profile 分组、配置覆盖顺序、`spring.config.import` 引入本地覆盖文件、按环境装配 Bean。

## 2. 出现频率与推荐度

- 频率：高频
- 推荐度：推荐
- 整理优先级：P0

## 3. 依赖与版本

- `springboot-common` + `spring-boot-starter-web`
- Boot 3.5.16 Config Data / Profiles / `spring.config.import`

## 4. 关键配置

- 默认 `spring.profiles.active=dev`
- `spring.profiles.group.production = proddb, prodmq`
- `spring.config.import: optional:file:./config/local-override.yml`
- 示例见 [`config/local-override.yml.example`](config/local-override.yml.example)

## 5. 核心代码要点

| 能力 | 入口 |
|---|---|
| 分层 yml | `application-*.yml` |
| group | `production` → `proddb`+`prodmq` |
| 多文档 | `spring.config.activate.on-profile` |
| import | optional file |
| `@Profile` | `DevEnvBanner` / `ProductionEnvBanner` / `TestEnvBanner` |
| 观测 | `/api/profiles/overview` |

## 6. 验证步骤

```bash
cmd /c "mvn -pl springboot-profiles -am spring-boot:run"
# production 组：
cmd /c "mvn -pl springboot-profiles spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=production"
```

见 [docs/06-验证步骤.md](docs/06-验证步骤.md)。

## 7. 常见坑与排障

见 [docs/07-常见坑与排障.md](docs/07-常见坑与排障.md)。

## 8. 对照方案

见 [docs/08-对照方案.md](docs/08-对照方案.md)。