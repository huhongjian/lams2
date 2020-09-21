package com.sogou.admin.workorder.workflow.service.process;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sogou.admin.workorder.workflow.dto.ProcessDto;
import com.sogou.admin.workorder.workflow.dto.ProcessQueryDto;

/**
 * 工作流流程服务
 *
 * @author shanglonghua
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
	 * 查询指定用户参与的流程信息
	 *
	 * @param queryDto
	 *            查询对象
	 * @return 流程信息集合
	 */
	List<ProcessDto> getHistoryProcessInstance(ProcessQueryDto queryDto);

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
