package com.xiesn.springboot.docker.compose;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.nio.file.Path;
import java.util.Map;

/**
 * 用系统属性 + 最高优先级 PropertySource 覆盖 application.yml 中的相对路径。
 * <p>{@code setDefaultProperties} 优先级低于 yml，不能用；必须用 {@link System#setProperty}。</p>
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ComposeFileEnvironmentPostProcessor implements EnvironmentPostProcessor {

    /**
     * 写入绝对路径。
     *
     * @param environment 环境
     * @param application 应用
     */
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Path composeFile = ComposeFileLocator.requireComposeFile();
        String absolute = composeFile.toString();
        // 系统属性优先级高于 application.yml，确保 DockerComposeProperties 绑到绝对路径
        System.setProperty("spring.docker.compose.file", absolute);
        environment.getPropertySources().addFirst(new MapPropertySource(
                "dockerComposeAbsoluteFile",
                Map.of("spring.docker.compose.file", absolute)));
        System.out.println("[docker-compose] resolved compose file -> " + absolute);
    }
}