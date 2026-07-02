package com.barber.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * ClassName: CacheStartupListener
 * Description: 项目启动时，初始化缓存
 */
@Component
public class CacheStartupListener implements ApplicationListener<ApplicationReadyEvent> {

    /**
     * 项目启动时，初始化缓存
     * project startup, initialize cache
     * @param applicationReadyEvent
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        // System.out.println("Logistics Config Api Cache initialization completed.");
    }
}
