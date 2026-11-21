package com.xiesn.springboot.lifecycle.bean;

import com.xiesn.springboot.lifecycle.timeline.LifecycleTimeline;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * Bean 生命周期回调演示。
 * <p>典型顺序（简化）：构造 → {@code BeanNameAware} → {@code @PostConstruct}
 * → {@code InitializingBean#afterPropertiesSet} → … → {@code @PreDestroy}
 * → {@code DisposableBean#destroy}。</p>
 */
@Component
public class DemoLifecycleBean implements BeanNameAware, InitializingBean, DisposableBean {

    /** Spring 分配的 bean 名称 */
    private String beanName;

    /**
     * 无参构造，观察构造时机。
     */
    public DemoLifecycleBean() {
        LifecycleTimeline.record("bean", "DemoLifecycleBean.<init>", "构造方法执行");
    }

    /**
     * Aware 回调：注入 bean 名称，早于初始化。
     *
     * @param name bean 名
     */
    @Override
    public void setBeanName(String name) {
        this.beanName = name;
        LifecycleTimeline.record("bean", "BeanNameAware", "beanName=" + name);
    }

    /**
     * JSR-250 初始化回调。
     */
    @PostConstruct
    public void postConstruct() {
        LifecycleTimeline.record("bean", "@PostConstruct", "beanName=" + beanName);
    }

    /**
     * Spring 初始化回调。
     */
    @Override
    public void afterPropertiesSet() {
        LifecycleTimeline.record("bean", "InitializingBean.afterPropertiesSet", "属性注入完成，可做自检");
    }

    /**
     * JSR-250 销毁回调。
     */
    @PreDestroy
    public void preDestroy() {
        LifecycleTimeline.record("shutdown", "@PreDestroy", "释放资源前最后机会");
    }

    /**
     * Spring 销毁回调。
     */
    @Override
    public void destroy() {
        LifecycleTimeline.record("shutdown", "DisposableBean.destroy", "DisposableBean 销毁回调");
    }

    /**
     * 供接口展示当前 bean 名。
     *
     * @return bean 名
     */
    public String getBeanName() {
        return beanName;
    }
}
