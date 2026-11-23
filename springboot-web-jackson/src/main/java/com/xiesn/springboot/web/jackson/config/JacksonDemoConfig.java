package com.xiesn.springboot.web.jackson.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

/**
 * 补上 {@code spring.jackson.date-format} 盖不住的 Java 8 时间格式。
 * <p>空值和 {@code java.util.Date} 仍放在 {@code application.yml}，避免两处各写一套。</p>
 */
@Configuration
public class JacksonDemoConfig {

    /** 全局 {@link java.time.LocalDateTime} 格式。字段上的 {@code @JsonFormat} 可以再覆盖。 */
    public static final DateTimeFormatter DATE_TIME =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 注册 Boot 自动配置的 ObjectMapper 定制器。
     *
     * @return Java 8 时间序列化定制
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer localDateTimeCustomizer() {
        return builder -> builder
                .serializers(new LocalDateTimeSerializer(DATE_TIME))
                .deserializers(new LocalDateTimeDeserializer(DATE_TIME));
    }
}
