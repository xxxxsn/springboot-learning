package com.xiesn.springboot.common.result;

/**
 * 业务错误码约定。
 * <p>各业务模块可实现本接口扩展专属错误码，避免魔法数字散落在代码里。</p>
 */
public interface ErrorCode {

    /**
     * 获取数字错误码。
     *
     * @return 错误码，成功一般为 0
     */
    int getCode();

    /**
     * 获取对外展示的错误信息。
     *
     * @return 可读消息
     */
    String getMessage();
}
