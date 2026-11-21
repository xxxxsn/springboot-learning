package com.xiesn.springboot.docker.compose;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Path;

/**
 * spring-boot-docker-compose 演示启动类。
 */
@SpringBootApplication
public class DockerComposeApplication {

    /**
     * 应用入口：先设置 compose 绝对路径到系统属性（高于 application.yml）。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        Path composeFile = ComposeFileLocator.requireComposeFile();
        System.setProperty("spring.docker.compose.file", composeFile.toString());
        System.out.println("[docker-compose] main set compose file -> " + composeFile);
        SpringApplication.run(DockerComposeApplication.class, args);
    }
}