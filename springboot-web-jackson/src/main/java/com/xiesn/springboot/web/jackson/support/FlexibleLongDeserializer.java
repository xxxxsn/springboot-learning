package com.xiesn.springboot.web.jackson.support;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * 雪花 ID 反序列化：JSON 字符串和整数都能读。
 * <p>只挂在主键字段上。金额等业务数字保持 JSON number。</p>
 */
public class FlexibleLongDeserializer extends JsonDeserializer<Long> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Long deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonToken token = parser.currentToken();
        if (token == JsonToken.VALUE_NULL) {
            return null;
        }
        if (token == JsonToken.VALUE_NUMBER_INT) {
            return parser.getLongValue();
        }
        if (token == JsonToken.VALUE_STRING) {
            return parseText(parser.getText(), context);
        }
        throw context.wrongTokenException(parser, Long.class, JsonToken.VALUE_STRING, "Long 需为字符串或整数");
    }

    /**
     * 解析字符串形式的 Long。空串当 null。
     *
     * @param text    原始文本
     * @param context 反序列化上下文
     * @return Long 值
     */
    private static Long parseText(String text, DeserializationContext context) throws IOException {
        if (text == null || text.isBlank()) {
            return null;
        }
        try {
            return Long.parseLong(text.trim());
        } catch (NumberFormatException ex) {
            throw context.weirdStringException(text, Long.class, "不是合法的 Long");
        }
    }
}
