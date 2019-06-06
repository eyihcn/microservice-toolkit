package com.fuli.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;


/**
 * @Author: chenyi
 * @CreateDate: 2019/4/19 11:54
 */
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class,DruidDataSourceAutoConfigure.class,SecurityAutoConfiguration.class})
@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
@EnableAsync
@EnableScheduling
public class FuliCloudAppMessageApplication {

    public static void main(String[] args) {
        SpringApplication.run(FuliCloudAppMessageApplication.class, args);
    }


    @Bean("messageScheduledTaskPool")
    public TaskScheduler fileProcessScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("messageScheduledTaskPool-");
        scheduler.setPoolSize(2);
        scheduler.initialize();
        return scheduler;
    }

}
