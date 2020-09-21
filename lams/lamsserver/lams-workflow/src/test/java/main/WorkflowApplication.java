package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * spring容器（供本模块单测使用）
 * 
 * @author shanglonghua
 * 
 */
@SpringBootApplication
@ComponentScan("com.sogou.admin.workorder")
public class WorkflowApplication {
	public static void main(String[] args) {
		SpringApplication.run(WorkflowApplication.class, args);
	}
}
