package com.xiesn.springboot.i18n;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 国际化演示启动类。
 * <p>推荐做法是接口只返回 i18n key；{@code MessageSource} 仅作为服务端直接出文案的对照。</p>
 */
@SpringBootApplication
public class I18nApplication {

    /**
     * 应用入口。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(I18nApplication.class, args);
    }
}
