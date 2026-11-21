package com.xiesn.springboot.spi.plugin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Java SPI / Spring SPI / 插件化演示启动类。
 * <p>启动后访问 {@code /api/spi/overview} 对照三种发现机制。</p>
 */
@SpringBootApplication
public class SpiPluginApplication {

    /**
     * 应用入口。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(SpiPluginApplication.class, args);
    }
}