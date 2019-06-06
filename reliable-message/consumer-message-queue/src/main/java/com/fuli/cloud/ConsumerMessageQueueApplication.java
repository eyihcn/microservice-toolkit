package com.fuli.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;

/**
 * @Author: chenyi
 * @CreateDate: 2019/4/19 11:51
 */
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class,DruidDataSourceAutoConfigure.class})
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class ConsumerMessageQueueApplication {

	
    public static void main(String[] args) {
        SpringApplication.run(ConsumerMessageQueueApplication.class, args);
    }
    
}
