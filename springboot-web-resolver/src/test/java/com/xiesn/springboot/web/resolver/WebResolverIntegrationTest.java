package com.xiesn.springboot.web.resolver;

import com.xiesn.springboot.web.resolver.support.LoginUserAttrs;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * {@code @CurrentUser} 与 {@code @ApiResult} 的请求对照。
 */
@SpringBootTest
@AutoConfigureMockMvc
class WebResolverIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * 总览不要求登录。
     */
    @Test
    void overview_shouldExplainRules() throws Exception {
        mockMvc.perform(get("/api/resolver/overview"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.port").value(8028))
                .andExpect(jsonPath("$.data.userIdHeader").value(LoginUserAttrs.USER_ID_HEADER));
    }

    /**
     * 必填用户缺失时走 common 的未认证码。
     */
    @Test
    void me_withoutUser_shouldBeUnauthorized() throws Exception {
        mockMvc.perform(get("/api/resolver/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.message").value("缺少当前用户"));
    }

    /**
     * 请求头中的用户被注入，并且返回值包进 R。
     */
    @Test
    void me_withUser_shouldWrapProfile() throws Exception {
        mockMvc.perform(get("/api/resolver/me")
                        .header(LoginUserAttrs.USER_ID_HEADER, "7")
                        .header(LoginUserAttrs.USER_NAME_HEADER, "ada"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.id").value(7))
                .andExpect(jsonPath("$.data.name").value("ada"))
                .andExpect(jsonPath("$.data.guest").value(false));
    }

    /**
     * 可选用户在没有请求头时是访客。
     */
    @Test
    void optional_withoutUser_shouldBeGuest() throws Exception {
        mockMvc.perform(get("/api/resolver/me/optional"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.name").value("guest"))
                .andExpect(jsonPath("$.data.guest").value(true))
                .andExpect(jsonPath("$.data.id").doesNotExist());
    }

    /**
     * 已经是 R 的返回值不再套一层。
     */
    @Test
    void alreadyWrapped_shouldStaySingleEnvelope() throws Exception {
        mockMvc.perform(get("/api/resolver/already-wrapped"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data").value("kept"));
    }

    /**
     * 没有 @ApiResult 时不出现统一信封。
     */
    @Test
    void plain_shouldSkipEnvelope() throws Exception {
        mockMvc.perform(get("/api/resolver/plain")
                        .header(LoginUserAttrs.USER_ID_HEADER, "7")
                        .header(LoginUserAttrs.USER_NAME_HEADER, "ada"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").doesNotExist())
                .andExpect(jsonPath("$.name").value("ada"))
                .andExpect(jsonPath("$.guest").value(false));
    }
}
