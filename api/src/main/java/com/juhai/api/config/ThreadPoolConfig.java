package com.juhai.api.config;


import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Data
@Configuration
public class ThreadPoolConfig {

    private Integer coreSize = 50;

    private Integer maxSize = 200;

    private Integer keepAliveTime = 300;

    /**
     * 业务线程池，所有业务代码需要开启子线程的，都应该使用线程池创建，不要使用new Thread();
     * corePoolSize
     * maximumPoolSize
     * keepAliveTime
     * TimeUnit
     * workQueue
     * threadFactory
     * handler
     * @return
     */
    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        return new ThreadPoolExecutor(
                coreSize,
                maxSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(100000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }
}
