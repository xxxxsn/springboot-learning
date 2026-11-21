# springboot-web-i18n

> 编号：31 · 域 02 · **状态：已完成** · 端口：**8031**  
> 展开笔记：[docs/](docs/) · 自测：[api.http](api.http) · 总进度：[docs/进度.md](../docs/进度.md)

## 1. 适用场景

前后端分离时，接口失败不要回中文句子。`message` 放 i18n key，后台 vue-i18n、前台 Nuxt i18n 各自翻译。服务端模板或邮件才用 `MessageSource`。

## 2. 出现频率与推荐度

- 频率：高频
- 推荐度：推荐（key）；`MessageSource` 为对照
- 整理优先级：P0

## 3. 依赖与版本

- Spring Boot 3.5.16 自带的 `MessageSource`（`spring-boot-starter-web`）
- `spring-boot-starter-validation`：演示参数校验收成同一个 key
- 不引入 vue 或 Nuxt 工程。前端文案放在 `src/main/resources/frontend-catalog/`

详见 [docs/03-依赖与版本.md](docs/03-依赖与版本.md)。

## 4. 关键配置

端口 `8031`。`spring.messages.basename=messages`，`fallback-to-system-locale=false`。  
完整说明：[docs/04-关键配置.md](docs/04-关键配置.md)。

## 5. 核心代码要点

| 类 | 作用 |
|---|---|
| `I18nErrorCode` | 数字码 + key，例如 `41001` / `auth.login.invalid_credentials` |
| `I18nDemoService` | 登录口令、扩展名白名单 |
| `I18nExceptionHandler` | 校验失败统一成 `common.param_invalid`，盖住 common 的中文拼接 |
| `I18nMessageService` | 对照：按 `Accept-Language` 译成句子 |

细节：[docs/05-核心代码要点.md](docs/05-核心代码要点.md)。

## 6. 验证步骤

`mvn -pl springboot-web-i18n -am test`，或启动后按 [api.http](api.http) 点。教学口令是 `secret`。  
步骤：[docs/06-验证步骤.md](docs/06-验证步骤.md)。

## 7. 常见坑与排障

见 [docs/07-常见坑与排障.md](docs/07-常见坑与排障.md)。

## 8. 对照方案

见 [docs/08-对照方案.md](docs/08-对照方案.md)。

## 扩展：后台 vue-i18n、前台 Nuxt i18n 各自翻译

接口失败时 `message` 是 key，例如 `auth.login.invalid_credentials`。页面上要显示的句子不在这个字段里，要到当前端自己的语言包里找。

两边的查找方式一样：key 前面加上 `errors.`，变成 `errors.auth.login.invalid_credentials`。本模块的样例文案在：

- 后台：[frontend-catalog/vue/zh-CN.yaml](src/main/resources/frontend-catalog/vue/zh-CN.yaml)、[en.yaml](src/main/resources/frontend-catalog/vue/en.yaml)
- 前台：[frontend-catalog/nuxt/zh-CN.json](src/main/resources/frontend-catalog/nuxt/zh-CN.json)、[en.json](src/main/resources/frontend-catalog/nuxt/en.json)

新增一个 key 时，中文和英文文件一起补，缺一处界面就会露出整段 key。

### 后台：Vue 3 + vue-i18n

框架已经是 Vue 3 时，文案翻译用 vue-i18n，不要自己写 if 判断语言。

1. 安装依赖：`npm install vue-i18n`。Vue 3 使用 vue-i18n 9 或更高。
2. 把上面两份 yaml 拷到项目的 `locales/zh-CN.yaml`、`locales/en.yaml`。yaml 靠缩进表示层级，`errors` 下面的 `auth.login.invalid_credentials` 必须和接口返回的 key 对得上。
3. 启动时创建实例并挂到应用上。`legacy: false` 才能在 `<script setup>` 里用 `useI18n()`。

```ts
import { createI18n } from "vue-i18n";
import zhCN from "./locales/zh-CN.yaml";
import en from "./locales/en.yaml";

export const i18n = createI18n({
  legacy: false,
  locale: "zh-CN",
  fallbackLocale: "zh-CN",
  messages: { "zh-CN": zhCN, en }
});

app.use(i18n);
```

Vite 读 yaml 需要 `@vitejs/plugin-vue` 之外再能 import yaml。若项目还不能 `import "*.yaml"`，先把样例改成同结构的 json，`messages` 里换成 json 的默认导出。vue-pure-admin 一类模板会用 `import.meta.glob("../../locales/*.y(a)?ml")` 自动扫这个目录，不必手写两行 import。

4. 页面文案在组件里取。模板中用 `$t('errors.file.type_not_allowed')`；脚本里：

```ts
const { t } = useI18n();
t("errors.file.type_not_allowed");
```

5. 接口错误不要直接 `alert(body.message)`。`message` 是 key。请求封装里统一翻译，找不到 key 时用一条兜底文案，避免把 key 甩到屏幕上：

```ts
const { t, te } = i18n.global;
const key = `errors.${body.message}`;
const text = te(key) ? t(key) : t("errors.unknown");
```

`te` 是「这个 key 在当前语言包里有没有」。`errors.unknown` 要在两份语言包里都写上，例如「未知错误（{code}）」。

6. 切换语言只改当前 locale，不必刷新页面：

```ts
const { locale } = useI18n();
locale.value = locale.value === "zh-CN" ? "en" : "zh-CN";
```

用了 Element Plus 时，组件库自带的「确定 / 分页」不会跟你的 yaml 走。把 `element-plus/es/locale/lang/zh-cn` 和 `en` 合并进对应的 `messages`，切换时 `<el-config-provider :locale="elementLocale">` 一起换。

### 前台：Nuxt + @nuxtjs/i18n

Nuxt 不要再手动 `createI18n`。官方模块会扫描语言文件并生成 `useI18n()`。

1. 安装：`npx nuxi module add @nuxtjs/i18n`，或 `npm install @nuxtjs/i18n` 后自己写入 `nuxt.config`。
2. 把两份 json 放到 `i18n/locales/zh-CN.json`、`i18n/locales/en.json`。目录名用 `i18n/locales`，模块默认会读这里。
3. 在 `nuxt.config.ts` 声明语言。默认中文不加 URL 前缀，英文走 `/en/...`，并关掉按浏览器语言自动跳转，避免用户点一次中文又被弹回英文。

```ts
export default defineNuxtConfig({
  modules: ["@nuxtjs/i18n"],
  i18n: {
    locales: [
      { code: "zh-CN", language: "zh-CN", name: "简体中文", file: "zh-CN.json" },
      { code: "en", language: "en", name: "English", file: "en.json" }
    ],
    defaultLocale: "zh-CN",
    strategy: "prefix_except_default",
    detectBrowserLanguage: false
  }
});
```

4. 页面里直接用，不用 import。`useI18n` 由模块自动导入：

```vue
<script setup lang="ts">
const { t } = useI18n();
</script>

<template>
  <h1>{{ t("site.name") }}</h1>
</template>
```

按钮、标题、空状态放在 json 里，不要写死在模板里。带数字的句子用占位符，例如 `"questionCount": "{count} 道题"`，调用 `t("bank.questionCount", { count: 12 })`。

5. 登录、保存失败时，和后台同一条规则。接口 `message` 仍是 key：

```ts
const { t, te } = useI18n();

function errorText(messageKey: string) {
  const key = `errors.${messageKey}`;
  return te(key) ? t(key) : t("errors.unknown");
}
```

6. 跳转不要写死 `navigateTo("/login")`。英文站的登录页实际是 `/en/login`。用模块提供的 `useLocalePath()` 生成带当前语言前缀的路径：

```ts
const localePath = useLocalePath();
await navigateTo(localePath("/login"));
```

切换语言用 `useSwitchLocalePath()` 得到「当前页的另一种语言地址」，再 `navigateTo` 过去。这样查询参数还在，只是前缀从无变成 `/en`，或从 `/en` 去掉。

```ts
const locale = useI18n().locale;
const switchLocalePath = useSwitchLocalePath();
const target = locale.value === "zh-CN" ? "en" : "zh-CN";
await navigateTo(switchLocalePath(target));
```

7. 改完 json 看不到新句子时，先停掉 `nuxt dev` 再开一次。语言文件是构建期收进去的，热更新不一定扫到新增的 key。
