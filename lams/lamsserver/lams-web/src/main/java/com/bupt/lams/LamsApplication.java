package com.bupt.lams;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableCaching
@MapperScan(basePackages = "com.bupt.lams.mapper")
@EnableScheduling
public class LamsApplication {

    public static void main(String[] args) {
        SpringApplication.run(LamsApplication.class, args);
    }

    /**
     * 引入了websocket和activiti，存在多个excutor，选定一个主要的
     *
     * @return
     */
    @Primary
    @Bean
    public TaskExecutor primaryTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        return executor;
    }

}