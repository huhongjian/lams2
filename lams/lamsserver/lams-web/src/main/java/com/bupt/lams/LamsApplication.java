package com.bupt.lams;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.apdplat.word.analysis.CosineTextSimilarity;
import org.apdplat.word.analysis.TextSimilarity;
import org.apdplat.word.segmentation.SegmentationAlgorithm;
import org.apdplat.word.util.WordConfTools;
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

//    /**
//     * 预热分词器字典，牺牲部署时间，节约接口耗时
//     *
//     * @return
//     */
//    @Bean
//    public CosineTextSimilarity cosineTextSimilarity() {
//        CosineTextSimilarity textSimilarity = new CosineTextSimilarity();
//        textSimilarity.similarScore("我爱我的祖国，我爱我的家乡", "我爱读书，我爱玩游戏");
//        System.out.println("----------------------------字典预热完成----------------------------\n");
//        return textSimilarity;
//    }

}