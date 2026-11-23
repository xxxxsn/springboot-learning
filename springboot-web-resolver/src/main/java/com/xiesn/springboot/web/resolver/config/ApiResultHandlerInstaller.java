package com.xiesn.springboot.web.resolver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiesn.springboot.web.resolver.mvc.ApiResultReturnValueHandler;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 把 {@code @ApiResult} 处理器插到内置返回值处理器之前。
 * <p>不实现 {@code WebMvcConfigurer}，避免和 {@code RequestMappingHandlerAdapter} 互相依赖。
 * {@code addReturnValueHandlers} 排在 {@code @ResponseBody} 之后，对 {@code @RestController} 不生效。</p>
 */
@Component
public class ApiResultHandlerInstaller {

    private static final Logger log = LoggerFactory.getLogger(ApiResultHandlerInstaller.class);

    private final RequestMappingHandlerAdapter handlerAdapter;
    private final ObjectMapper objectMapper;

    /**
     * @param handlerAdapter 已完成初始化的方法适配器
     * @param objectMapper   写出 JSON 的 mapper
     */
    public ApiResultHandlerInstaller(RequestMappingHandlerAdapter handlerAdapter, ObjectMapper objectMapper) {
        this.handlerAdapter = handlerAdapter;
        this.objectMapper = objectMapper;
    }

    /**
     * 适配器初始化之后再改返回值处理器列表。
     */
    @PostConstruct
    public void install() {
        List<HandlerMethodReturnValueHandler> existing = handlerAdapter.getReturnValueHandlers();
        if (existing == null || containsApiResultHandler(existing)) {
            return;
        }
        List<HandlerMethodReturnValueHandler> handlers = new ArrayList<>(existing.size() + 1);
        handlers.add(new ApiResultReturnValueHandler(objectMapper));
        handlers.addAll(existing);
        handlerAdapter.setReturnValueHandlers(handlers);
        log.info("[resolver] ApiResult 返回值处理器已插入首位，共 {} 个", handlers.size());
    }

    /**
     * 避免重复插入。
     *
     * @param handlers 当前处理器
     * @return 是否已经有 ApiResult 处理器
     */
    private static boolean containsApiResultHandler(List<HandlerMethodReturnValueHandler> handlers) {
        for (HandlerMethodReturnValueHandler handler : handlers) {
            if (handler instanceof ApiResultReturnValueHandler) {
                return true;
            }
        }
        return false;
    }
}
