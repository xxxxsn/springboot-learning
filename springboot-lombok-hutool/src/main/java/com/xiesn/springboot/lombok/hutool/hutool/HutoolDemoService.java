package com.xiesn.springboot.lombok.hutool.hutool;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import com.xiesn.springboot.lombok.hutool.hutool.model.PersonSource;
import com.xiesn.springboot.lombok.hutool.hutool.model.PersonTarget;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Hutool 高频工具演示。
 */
@Service
public class HutoolDemoService {

    /**
     * 一次返回全部样例。
     *
     * @return 汇总
     */
    public Map<String, Object> all() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("str", demoStr());
        result.put("date", demoDate());
        result.put("id", demoId());
        result.put("digest", demoDigest("hello-hutool"));
        result.put("bean", demoBean());
        result.put("json", demoJson());
        return result;
    }

    /**
     * 字符串与集合工具。
     *
     * @return 演示结果
     */
    public Map<String, Object> demoStr() {
        String raw = "  a,b,,c  ";
        List<String> parts = StrUtil.splitTrim(raw, ',');
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("case", "StrUtil / CollUtil");
        result.put("input", raw);
        result.put("output", Map.of(
                "isBlank", StrUtil.isBlank("   "),
                "trim", StrUtil.trim(raw),
                "splitTrim", parts,
                "collIsEmpty", CollUtil.isEmpty(List.of()),
                "join", CollUtil.join(parts, "|")));
        result.put("pitfall", "blank 判断与 Spring StringUtils 语义接近；业务断言仍建议走 common AssertUtils");
        return result;
    }

    /**
     * 日期格式化与偏移。
     *
     * @return 演示结果
     */
    public Map<String, Object> demoDate() {
        LocalDateTime now = LocalDateTimeUtil.now();
        LocalDateTime plusDay = now.plusDays(1);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("case", "DateUtil / LocalDateTimeUtil");
        result.put("input", "now");
        result.put("output", Map.of(
                "formatDateTime", DateUtil.formatDateTime(DateUtil.date()),
                "localFormat", LocalDateTimeUtil.formatNormal(now),
                "plusOneDay", LocalDateTimeUtil.formatNormal(plusDay),
                "offsetDay", DateUtil.offset(DateUtil.date(), DateField.DAY_OF_YEAR, 1).toString()));
        result.put("pitfall", "新代码优先 LocalDateTime；DateUtil 适合遗留 Date API");
        return result;
    }

    /**
     * UUID 与雪花 ID。
     *
     * @return 演示结果
     */
    public Map<String, Object> demoId() {
        // 演示固定 worker/datacenter，避免多实例冲突时再按机房分配
        long snowflake = IdUtil.getSnowflake(1, 1).nextId();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("case", "IdUtil");
        result.put("input", Map.of("workerId", 1, "datacenterId", 1));
        result.put("output", Map.of(
                "fastSimpleUUID", IdUtil.fastSimpleUUID(),
                "snowflakeId", snowflake));
        result.put("pitfall", "雪花依赖机器时钟与 workerId 唯一；多实例必须规划 workerId，勿硬编码同一值");
        return result;
    }

    /**
     * 摘要算法（不是加密）。
     *
     * @param text 原文
     * @return 演示结果
     */
    public Map<String, Object> demoDigest(String text) {
        String safe = StrUtil.blankToDefault(text, "hello-hutool");
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("case", "DigestUtil（摘要，非可逆加密）");
        result.put("input", safe);
        result.put("output", Map.of(
                "md5Hex", DigestUtil.md5Hex(safe),
                "sha256Hex", DigestUtil.sha256Hex(safe)));
        result.put("pitfall", "MD5/SHA 只能做校验或指纹，不能当密码存储方案；密码请用 BCrypt/Argon2");
        return result;
    }

    /**
     * Bean 属性拷贝。
     *
     * @return 演示结果
     */
    public Map<String, Object> demoBean() {
        PersonSource source = new PersonSource();
        source.setName("王五");
        source.setAge(28);
        source.setCity("上海");
        PersonTarget target = BeanUtil.copyProperties(source, PersonTarget.class);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("case", "BeanUtil.copyProperties");
        result.put("input", Map.of("name", source.getName(), "age", source.getAge(), "city", source.getCity()));
        result.put("output", Map.of(
                "targetName", target.getName(),
                "targetAge", target.getAge(),
                "targetCity", target.getCity()));
        result.put("pitfall", "同名同类型才拷贝；嵌套对象默认浅拷贝，复杂映射更适合 MapStruct");
        return result;
    }

    /**
     * JSON 序列化与反序列化。
     *
     * @return 演示结果
     */
    public Map<String, Object> demoJson() {
        Map<String, Object> bean = Map.of("sku", "A01", "qty", 3);
        String json = JSONUtil.toJsonStr(bean);
        Map<?, ?> parsed = JSONUtil.toBean(json, Map.class);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("case", "JSONUtil");
        result.put("input", bean);
        result.put("output", Map.of("json", json, "parsed", parsed));
        result.put("pitfall", "Spring MVC 默认 Jackson；Hutool JSON 适合脚本式处理，与全局 ObjectMapper 配置不共享");
        return result;
    }
}
