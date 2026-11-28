package com.xiesn.springboot.gradle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Gradle 对照模块骨架。建议用 Gradle 启动，不纳入父 Maven modules。
 */
@SpringBootApplication
public class GradleApplication {
    public static void main(String[] args) {
        SpringApplication.run(GradleApplication.class, args);
    }
}
