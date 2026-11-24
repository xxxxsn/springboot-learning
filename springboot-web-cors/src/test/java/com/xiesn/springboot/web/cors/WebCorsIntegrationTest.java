package com.xiesn.springboot.web.cors;

import com.xiesn.springboot.web.cors.support.CorsOrigins;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.cors.CorsConfiguration;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 全局白名单、方法级并集、未配置路径和通配源加凭证。
 */
@SpringBootTest
@AutoConfigureMockMvc
class WebCorsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * 白名单源站得到回显的 Origin、凭证和可暴露响应头。
     */
    @Test
    void adminOrigin_shouldEchoAllowOrigin() throws Exception {
        mockMvc.perform(get("/api/cors/overview").header(HttpHeaders.ORIGIN, CorsOrigins.ADMIN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.port").value(8029))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, CorsOrigins.ADMIN))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true"))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, CorsOrigins.EXPOSED_HEADER))
                .andExpect(header().string(CorsOrigins.EXPOSED_HEADER, "demo-8029"));
    }

    /**
     * 没有 Origin 时按普通请求处理，不写跨域响应头。
     */
    @Test
    void sameOrigin_shouldOmitCorsHeaders() throws Exception {
        mockMvc.perform(get("/api/cors/overview"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, nullValue()));
    }

    /**
     * 全局未列出的源站被拒绝。
     */
    @Test
    void unknownOrigin_shouldBeForbidden() throws Exception {
        mockMvc.perform(get("/api/cors/overview").header(HttpHeaders.ORIGIN, CorsOrigins.EVIL))
                .andExpect(status().isForbidden());
    }

    /**
     * 预检不进入控制器，只返回允许的方法、头和缓存时间。
     */
    @Test
    void preflight_shouldAllowPostFromAdmin() throws Exception {
        mockMvc.perform(options("/api/cors/messages")
                        .header(HttpHeaders.ORIGIN, CorsOrigins.ADMIN)
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "Content-Type"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, CorsOrigins.ADMIN))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true"))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, containsString("POST")))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, containsString("Content-Type")))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_MAX_AGE, String.valueOf(CorsOrigins.MAX_AGE_SECONDS)))
                .andExpect(jsonPath("$.code").doesNotExist());
    }

    /**
     * {@code @CrossOrigin} 与全局源站取并集：商城和管理端都能访问 /shop。
     */
    @Test
    void shop_shouldAllowAnnotatedOriginAndGlobalOrigin() throws Exception {
        mockMvc.perform(get("/api/cors/shop").header(HttpHeaders.ORIGIN, CorsOrigins.SHOP))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.channel").value("shop"))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, CorsOrigins.SHOP));

        mockMvc.perform(get("/api/cors/shop").header(HttpHeaders.ORIGIN, CorsOrigins.ADMIN))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, CorsOrigins.ADMIN));
    }

    /**
     * 商城源不能访问只有全局配置的接口。
     */
    @Test
    void shopOrigin_shouldNotPassGlobalOnlyPath() throws Exception {
        mockMvc.perform(get("/api/cors/overview").header(HttpHeaders.ORIGIN, CorsOrigins.SHOP))
                .andExpect(status().isForbidden());
    }

    /**
     * 映射之外：实际请求仍 200 但不写 Allow-Origin；预检被 Spring 收成 403。
     */
    @Test
    void plainPath_shouldNotBecomeSuccessfulPreflight() throws Exception {
        mockMvc.perform(get("/api/plain/ping").header(HttpHeaders.ORIGIN, CorsOrigins.ADMIN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("pong"))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, nullValue()));

        mockMvc.perform(options("/api/plain/ping")
                        .header(HttpHeaders.ORIGIN, CorsOrigins.ADMIN)
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Invalid CORS request"))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, nullValue()));
    }

    /**
     * Framework 6.2 不在 setter 上报错，处理配置时才拒绝通配源加凭证。
     */
    @Test
    void wildcardOriginWithCredentials_shouldFailOnValidate() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.setAllowCredentials(true);
        assertThrows(IllegalArgumentException.class, configuration::validateAllowCredentials);
    }
}
