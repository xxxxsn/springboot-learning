# springboot-mapstruct

> 编号：30 · 域 02 · **状态：已完成（2026-11-24）** · 端口：**8030**  
> 展开笔记：[docs/](docs/) · 自测：[api.http](api.http) · 总进度：[docs/进度.md](../docs/进度.md)

## 1. 适用场景

接口不要直接吐数据库实体：主键要给前端字符串、金额库里是分、状态库里是枚举、地址和明细还要嵌套。手写 set 容易漏字段，BeanUtils 又是运行时才发现拷错。

## 2. 出现频率与推荐度

- 频率：高频
- 推荐度：推荐
- 整理优先级：P1

## 3. 依赖与版本

- MapStruct `1.6.3`（注解在编译期生成实现，不是反射）
- Lombok（实体要可变）+ `lombok-mapstruct-binding` `0.2.0`
- `springboot-common` 的统一 `R`

详见 [docs/03-依赖与版本.md](docs/03-依赖与版本.md)。

## 4. 关键配置

端口 `8030`。真正要配的是 `maven-compiler-plugin` 的 `annotationProcessorPaths`：MapStruct、Lombok、binding 三个都要写上。

详见 [docs/04-关键配置.md](docs/04-关键配置.md)。

## 5. 核心代码要点

| 类 | 作用 |
|---|---|
| `MoneyMapper` | 元 ↔ 分，拒绝超过两位小数 |
| `OrderMapper` | 创建、局部更新、详情、摘要、深拷贝 |
| `Order` / `Address` / `OrderItem` | 可变实体，给 `@MappingTarget` 用 |
| `OrderCreateRequest` / `OrderPatchRequest` | 入参 record |
| `OrderDetailVo` / `OrderSummaryVo` | 出参 record，id 为字符串 |
| `OrderMapstructService` | 只补主键、单号、创建时间 |

细节：[docs/05-核心代码要点.md](docs/05-核心代码要点.md)。

## 6. 验证步骤

```bash
mvn -B test -pl springboot-mapstruct -am
```

或启动后按 [api.http](api.http) 点 `GET /api/mapstruct/overview`。  
步骤：[docs/06-验证步骤.md](docs/06-验证步骤.md)。

## 怎么观察（学习用）

| 方式 | 看什么 |
|---|---|
| 接口优先 | `sampleDetail` 与 `afterPatchPreview`：金额单位、状态码、`createdAt` / `createdAtText`、null 有没有把原值冲掉 |
| 启动日志 | `[mapstruct] ready mapper=...OrderMapperImpl` |

总指南：[docs/学习观测指南.md](../docs/学习观测指南.md)

## 7. 常见坑与排障

见 [docs/07-常见坑与排障.md](docs/07-常见坑与排障.md)。

## 8. 对照方案

见 [docs/08-对照方案.md](docs/08-对照方案.md)。
