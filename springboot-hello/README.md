# springboot-hello

> 编号：2 · 域 01 · **状态：已完成（2026-11-20）** · 端口：**8002**  
> 展开笔记：[docs/](docs/) · 自测：[api.http](api.http) · 总进度：[docs/进度.md](../docs/进度.md)

## 1. 适用场景

Boot 3 起步：启动流程、自定义自动配置 / starter 机制、统一返回落地。

## 2. 出现频率与推荐度

- 频率：高频
- 推荐度：推荐
- 整理优先级：P0

## 3. 依赖与版本

- 依赖 `springboot-common`
- `spring-boot-starter-web`

## 4. 关键配置

```yaml
server.port: 8002
demo.hello.enabled: true
demo.hello.greeting: 你好
demo.hello.default-name: Spring Boot 3
```

## 5. 核心代码要点

- `HelloAutoConfiguration` + `AutoConfiguration.imports`：Boot 3 注册自动配置的正确方式
- `HelloProperties` / `HelloService`：条件装配与可覆盖 Bean
- `HelloStartupRunner`：`ApplicationRunner` 观察启动完成时机
- `HelloController`：统一 `R` 与业务异常演示
- 启动类排除对 AutoConfiguration 的组件扫描，强制走 imports

细节见 [docs/05-核心代码要点.md](docs/05-核心代码要点.md)。

## 6. 验证步骤

1. 启动 `HelloApplication`
2. 执行 `api.http`
3. 观察启动日志中的问候语

完整步骤：[docs/06-验证步骤.md](docs/06-验证步骤.md)。

## 7. 常见坑与排障

见 [docs/07-常见坑与排障.md](docs/07-常见坑与排障.md)。

## 8. 对照方案

见 [docs/08-对照方案.md](docs/08-对照方案.md)。
