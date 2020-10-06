package com.bupt.lams.config;

import com.bupt.lams.service.identity.impl.GroupDataCustomManager;
import com.bupt.lams.service.identity.impl.UserDataCustomManager;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;

/**
 * Activiti配置
 */
@Configuration
public class ActivitiConfig {

	@Resource
	private PlatformTransactionManager transactionManager;

	@Resource
	private DataSource dataSource;

	@Resource
	private ResourcePatternResolver resourceLoader;

	@Resource
	private GroupDataCustomManager groupDataCustomManager;

	@Resource
	private UserDataCustomManager userDataCustomManager;

	@Bean(name = "processEngineConfiguration")
	public SpringProcessEngineConfiguration getProcessEngineConfiguration() throws IOException {
		SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();
		org.springframework.core.io.Resource[] procDefResources = resourceLoader.getResources("classpath:processes/*.bpmn");
		configuration.setDatabaseSchemaUpdate("true");
		configuration.setDataSource(dataSource);
		configuration.setTransactionManager(transactionManager);
		configuration.setDeploymentResources(procDefResources);
		configuration.setDbIdentityUsed(false);
		configuration.setHistory("full");
		configuration.setGroupDataManager(groupDataCustomManager);
		configuration.setUserDataManager(userDataCustomManager);
		return configuration;
	}

	@Bean(name = "processEngine")
	public ProcessEngineFactoryBean getProcessEngineFactoryBean() throws IOException {
		ProcessEngineFactoryBean factoryBean = new ProcessEngineFactoryBean();
		factoryBean.setProcessEngineConfiguration(getProcessEngineConfiguration());
		return factoryBean;
	}

}
