package com.sogou.admin.workorder.workflow.service.repository.impl;

import com.sogou.admin.workorder.workflow.service.repository.ResourceManageService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ResourceManageServiceImpl implements ResourceManageService {

	private Logger logger = LoggerFactory.getLogger(ResourceManageServiceImpl.class);

	@Resource
	private RepositoryService repositoryService;

	@Override
	public String getNewProcessDefinitionIdByProcessDefintionKey(String processDefinitionKey) {
		logger.info("获取最新流程定义：" + processDefinitionKey);
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
		        .processDefinitionKey(processDefinitionKey).latestVersion().singleResult();
		if (processDefinition != null) {
			return processDefinition.getId();
		} else {
			return null;
		}
	}
}
