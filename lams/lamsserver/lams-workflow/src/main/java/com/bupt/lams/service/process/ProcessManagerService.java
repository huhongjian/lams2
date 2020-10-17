package com.bupt.lams.service.process;

import com.bupt.lams.dto.ProcessQueryDto;

import java.util.Map;
import java.util.Set;

/**
 * 工作流流程服务
 */
public interface ProcessManagerService {

	/**
	 * 流程启动
	 *
	 * @param processDefinitionKey
	 *            流程定义Key
	 * @param businessKey
	 *            业务编号
	 * @param variables
	 *            启动变量
	 * @param startUserId
	 *            启动账号
	 * @return 流程实例ID
	 */
	String submitStartFormDataByProcessDefinitionKey(String processDefinitionKey, String businessKey,
	        Map<String, String> variables, String startUserId);

	/**
	 * 查询指定用户参与的流程总数
	 *
	 * @param queryDto
	 *            查询对象
	 * @return 流程总数
	 */
	Long getHistoryProcessInstanceCount(ProcessQueryDto queryDto);

	/**
	 * 根据流程实例ID获取<ID, BusinessKey>映射关系
	 *
	 * @param procInstIds
	 *            流程实例ID集合
	 * @return <ID, BusinessKey>映射关系
	 */
	Map<String, String> getProcBizKeyMapById(Set<String> procInstIds);

	/**
	 * 挂起流程实例
	 *
	 * @param businessKey
	 *            业务Key
	 */
	void hungUpProcessInstanceByBizKey(String businessKey);

}
