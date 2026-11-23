package com.xiesn.springboot.web.jackson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Jackson 序列化演示启动类。
 * <p>对照日期格式、雪花 Long、枚举码和空值策略。参数解析见 #28。</p>
 */
@SpringBootApplication
public class WebJacksonApplication {

    /**
     * 应用入口。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(WebJacksonApplication.class, args);
    }
}
