package com.xiesn.springboot.mapstruct.support;

import com.xiesn.springboot.mapstruct.mapper.OrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 启动时打出生成类的名字，确认注解处理器真的跑过，并且 Spring 扫到了这个 Bean。
 */
@Component
public class MapstructStartupLogger {

    private static final Logger log = LoggerFactory.getLogger(MapstructStartupLogger.class);

    /**
     * @param orderMapper 生成的映射器
     */
    public MapstructStartupLogger(OrderMapper orderMapper) {
        log.info("[mapstruct] ready mapper={} port=8030", orderMapper.getClass().getName());
    }
}
