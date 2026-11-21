# springboot-web-restful

> 编号：23 · 域 02 · **状态：已完成（2026-11-21）** · 端口：**8023**  
> 展开笔记：[docs/](docs/) · 自测：[api.http](api.http) · 总进度：[docs/进度.md](../docs/进度.md)

## 1. 适用场景

对外 HTTP API 需要统一资源命名、动词语义、状态码与响应信封时；国内项目「HTTP 200 + 业务 code」与纯 REST「HTTP 语义对齐」对照选型。

## 2. 出现频率与推荐度

- 频率：高频
- 推荐度：推荐
- 整理优先级：P0

## 3. 依赖与版本

- `spring-boot-starter-web` + `validation`
- `springboot-common`：`R` / `PageResult` / 全局异常
- 无额外三方库

## 4. 关键配置

`server.port=8023`；无特殊 Boot 配置，约定主要体现在 Controller。

## 5. 核心代码要点

| 项 | 说明 |
|---|---|
| `/api/articles` | 复数资源 + CRUD + 分页 |
| POST | `201` + `Location` |
| DELETE | 成功 `204` 无 body |
| `/api/restful/styles/*` | 信封风格 vs HTTP 状态风格 |

## 6. 验证步骤

```bash
cmd /c "mvn -pl springboot-web-restful -am spring-boot:run"
```

见 [docs/06-验证步骤.md](docs/06-验证步骤.md)。

## 7. 常见坑与排障

见 [docs/07-常见坑与排障.md](docs/07-常见坑与排障.md)。

## 8. 对照方案

见 [docs/08-对照方案.md](docs/08-对照方案.md)。
