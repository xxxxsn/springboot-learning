package com.xiesn.springboot.web.cors.web;

import com.xiesn.springboot.common.result.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 不在 CORS 映射里的接口，用来对照「没配等于不写跨域响应头」。
 */
@RestController
@RequestMapping("/api/plain")
public class PlainPingController {

    /**
     * 同源或未配置跨域时的普通响应。
     *
     * @return 固定文本
     */
    @GetMapping("/ping")
    public R<String> ping() {
        return R.ok("pong");
    }
}
