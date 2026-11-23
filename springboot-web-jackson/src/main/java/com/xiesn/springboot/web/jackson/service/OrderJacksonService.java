package com.xiesn.springboot.web.jackson.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.xiesn.springboot.web.jackson.domain.EnterpriseOrder;
import com.xiesn.springboot.web.jackson.domain.PlainOrder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 组装同一笔订单在「企业 ObjectMapper」和「默认 ObjectMapper」下的差异。
 */
@Service
public class OrderJacksonService {

    /**
     * 只注册 JavaTimeModule，并关掉时间戳。
     * <p>不关 {@code WRITE_DATES_AS_TIMESTAMPS} 时，{@code LocalDateTime} 会变成 {@code [2026,11,23,8,30]}。</p>
     */
    private final ObjectMapper rawMapper = createRawMapper();

    /**
     * 企业样例。
     *
     * @return 带注解的订单
     */
    public EnterpriseOrder sampleEnterprise() {
        return EnterpriseOrder.sample();
    }

    /**
     * 总览：对象本身交给 MVC 的 ObjectMapper，另附一份默认 ObjectMapper 的 JSON 文本。
     *
     * @return 对照结果
     */
    public Map<String, Object> overview() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("port", 8026);
        body.put("sampleId", EnterpriseOrder.SAMPLE_ID);
        body.put("sampleIdAfterJsNumber", roundTripAsJsNumber(EnterpriseOrder.SAMPLE_ID));
        body.put("enterprise", sampleEnterprise());
        body.put("plain", PlainOrder.sample());
        body.put("rawPlainJson", writeRaw(PlainOrder.sample()));
        body.put("rules", List.of(
                "LocalDateTime 全局 yyyy-MM-dd HH:mm:ss；paidAt 用 @JsonFormat 覆盖",
                "java.util.Date 只吃 spring.jackson.date-format",
                "主键 Long 单独转字符串；amountCent 仍是数字",
                "OrderStatus 写数字码，读入兼容码和枚举名",
                "全局 non_null；channel 用 @JsonInclude(ALWAYS) 保留 null"
        ));
        return body;
    }

    /**
     * 模拟 JS Number 的精度损失。{@code JSON.parse} 后这个主键会变成另一个值。
     *
     * @param value 原始 Long
     * @return 按 double 往返后的值
     */
    static long roundTripAsJsNumber(long value) {
        return (long) (double) value;
    }

    /**
     * 未套企业格式的 ObjectMapper。
     *
     * @return 只做 Java 8 时间、不写时间戳的 mapper
     */
    private static ObjectMapper createRawMapper() {
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    /**
     * 用未定制的 ObjectMapper 写出对照 JSON。
     *
     * @param order 对照订单
     * @return JSON 文本
     */
    private String writeRaw(PlainOrder order) {
        try {
            return rawMapper.writeValueAsString(order);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("默认 ObjectMapper 写出失败", ex);
        }
    }
}
