package com.xiesn.springboot.web.resolver.config;

import com.xiesn.springboot.web.resolver.mvc.CurrentUserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 注册 {@code @CurrentUser} 参数解析器。
 * <p>自定义解析器位于 ModelAttribute 兜底之前，复杂类型参数可以落到这里。</p>
 */
@Configuration
public class ResolverWebConfig implements WebMvcConfigurer {

    /**
     * {@inheritDoc}
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CurrentUserArgumentResolver());
    }
}
