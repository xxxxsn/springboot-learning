package com.xiesn.springboot.hello.autoconfigure;

import com.xiesn.springboot.common.util.StringUtils;

/**
 * Hello 领域服务。
 * <p>由自动配置创建 Bean，业务代码只依赖本类型，不关心配置从哪来。</p>
 */
public class HelloService {

    private final HelloProperties properties;

    /**
     * 注入配置属性。
     *
     * @param properties Hello 配置
     */
    public HelloService(HelloProperties properties) {
        this.properties = properties;
    }

    /**
     * 生成问候语。
     *
     * @param name 称呼，可空
     * @return 完整问候句
     */
    public String greet(String name) {
        String displayName = StringUtils.defaultIfBlank(name, properties.getDefaultName());
        return properties.getGreeting() + "，" + displayName + "！";
    }

    /**
     * 返回当前生效配置的只读快照，便于排查自动配置是否加载成功。
     *
     * @return 配置快照
     */
    public HelloProperties currentProperties() {
        return properties;
    }
}
