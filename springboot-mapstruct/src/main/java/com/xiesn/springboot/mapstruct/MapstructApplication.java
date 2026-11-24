package com.xiesn.springboot.mapstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * MapStruct 对象转换演示启动类。
 * <p>DTO / VO / Entity 的映射在编译期生成，日期和 Long 的 JSON 形态见 #26。</p>
 */
@SpringBootApplication
public class MapstructApplication {

    /**
     * 应用入口。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(MapstructApplication.class, args);
    }
}
