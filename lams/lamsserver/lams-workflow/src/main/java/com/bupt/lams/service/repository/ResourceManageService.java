package com.bupt.lams.service.repository;

/**
 * 工作流资源相关Service
 */
public interface ResourceManageService {

	/**
	 * 获取最新流程定义
	 *
	 * @param processDefinitionKey
	 *            流程定义Key
	 * @return 最新流程定义ID
	 */
	String getNewProcessDefinitionIdByProcessDefintionKey(String processDefinitionKey);

}
