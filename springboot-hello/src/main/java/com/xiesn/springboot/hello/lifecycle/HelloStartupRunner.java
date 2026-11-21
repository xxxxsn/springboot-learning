package com.xiesn.springboot.hello.lifecycle;

import com.xiesn.springboot.hello.autoconfigure.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 启动完成后的回调示例。
 * <p>{@link ApplicationRunner} 在容器刷新完成后执行，适合做预热、自检，不要在这里阻塞太久。</p>
 */
@Component
@Order(100)
public class HelloStartupRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(HelloStartupRunner.class);

    private final HelloService helloService;

    /**
     * 注入 HelloService，确认自动配置已在 Runner 执行前完成。
     *
     * @param helloService Hello 服务
     */
    public HelloStartupRunner(HelloService helloService) {
        this.helloService = helloService;
    }

    /**
     * 应用启动完成后打印一条问候，证明 Bean 装配链路已打通。
     *
     * @param args 启动参数
     */
    @Override
    public void run(ApplicationArguments args) {
        log.info("启动流程已完成，自动配置问候语 = {}", helloService.greet("百科学员"));
    }
}
