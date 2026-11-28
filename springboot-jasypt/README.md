# springboot-jasypt

> 编号：11 · 域 01 · **状态：已完成（2026-11-20）** · 端口：**8011**  
> 展开笔记：[docs/](docs/) · 自测：[api.http](api.http) · 总进度：[docs/进度.md](../docs/进度.md)

## 1. 适用场景

配置文件中的数据库密码、第三方密钥等敏感项不想明文入库时，用 Jasypt 写成 `ENC(...)`，运行时自动解密。

## 2. 出现频率与推荐度

- 频率：高频
- 推荐度：推荐
- 整理优先级：P1

## 3. 依赖与版本

依据 [ulisesbocchio/jasypt-spring-boot](https://github.com/ulisesbocchio/jasypt-spring-boot)（Spring Boot 3.5+）：

- `jasypt-spring-boot-starter:4.0.4`
- 有 `@SpringBootApplication` 时引入 starter 即可全局启用可解密属性

详见 [docs/03-依赖与版本.md](docs/03-依赖与版本.md)。

## 4. 关键配置

端口：`8011`

```yaml
jasypt:
  encryptor:
    password: ${JASYPT_ENCRYPTOR_PASSWORD:springboot-learning-demo}  # 生产勿用默认值
    algorithm: PBEWITHHMACSHA512ANDAES_256

demo:
  secret:
    db-password: ENC(...)
```

教学默认主密钥：`springboot-learning-demo`。  
完整说明：[docs/04-关键配置.md](docs/04-关键配置.md)。

## 5. 核心代码要点

| 类 | 作用 |
|---|---|
| `DemoSecretProperties` | 绑定 `demo.secret.*`，注入时已是解密明文 |
| `JasyptCryptoService` | 使用 `lazyJasyptStringEncryptor` 加解密 |
| `JasyptController` | `/api/jasypt/secrets|encrypt|decrypt` |
| `JasyptStartupChecker` | 启动自检样例 ENC |

注意：4.0.4 默认 Bean 名是 **`lazyJasyptStringEncryptor`**。  
细节：[docs/05-核心代码要点.md](docs/05-核心代码要点.md)。

## 6. 验证步骤

1. 启动后日志应有「解密自检通过」
2. `GET /api/jasypt/secrets` → `dbPasswordDecrypted=MySecretDbPassword!`
3. 其余见 `api.http` 与 [docs/06-验证步骤.md](docs/06-验证步骤.md)

## 怎么观察（学习用）

| 方式 | 看什么 |
|---|---|
| 日志优先 | 启动：`Jasypt 解密自检通过` / `未通过` |
| 接口 | `GET /api/jasypt/secrets` 看解密后字段（勿提交真实密钥） |

总指南：[docs/学习观测指南.md](../docs/学习观测指南.md)

## 7. 常见坑与排障

见 [docs/07-常见坑与排障.md](docs/07-常见坑与排障.md)。

## 8. 对照方案

见 [docs/08-对照方案.md](docs/08-对照方案.md)。
