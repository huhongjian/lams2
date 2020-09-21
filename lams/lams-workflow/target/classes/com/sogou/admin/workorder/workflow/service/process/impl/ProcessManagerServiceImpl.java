package com.sogou.admin.workorder.workflow.service.process.impl;

import com.sogou.admin.workorder.workflow.convertor.ProcessDtoConvertor;
import com.sogou.admin.workorder.workflow.dto.ProcessDto;
import com.sogou.admin.workorder.workflow.dto.ProcessQueryDto;
import com.sogou.admin.workorder.workflow.service.process.ProcessManagerService;
import com.sogou.admin.workorder.workflow.service.repository.ResourceManageService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 流程服务实现类（流程启动、挂起、历史流程查询）
 *
 * @author shanglonghua
 */
@Service
public class ProcessManagerServiceImpl implements ProcessManagerService {

	private Logger logger = LoggerFactory.getLogger(ProcessManagerServiceImpl.class);

	@Resource
	private ProcessEngine processEngine;

	@Resource
	private ResourceManageService resourceManageService;

	@Resource
	private IdentityService identityService;

	@Resource
	private RuntimeService runtimeService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String submitStartFormDataByProcessDefinitionKey(String processDefinitionKey, String businessKey,
	        Map variables, String startUserId) {
		logger.info("开启工作流， processDefinitionKey ：" + processDefinitionKey + "; businessKey : " + businessKey
		        + "; variables : " + variables + "; startUserId : " + startUserId);
		String processDefinitionId = resourceManageService
		        .getNewProcessDefinitionIdByProcessDefintionKey(processDefinitionKey);
		this.identityService.setAuthenticatedUserId(startUserId);
		ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinitionId, businessKey,
		        variables);
		return processInstance.getProcessInstanceId();
	}

	@Override
	public List<ProcessDto> getHistoryProcessInstance(ProcessQueryDto queryDto) {

		logger.info("查询历史流程信息：" + queryDto.getUserName() + "; businessKey : " + queryDto.getBusinessKey());
		HistoryService historyService = processEngine.getHistoryService();
		// 1. 创建历史流程实例，查询对象
		HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery();
		// 2. 设置指定用户参与的流程信息
		historicProcessInstanceQuery.involvedUser(queryDto.getUserName());
		// 3. 查询获取数据列表
		List<HistoricProcessInstance> list = historicProcessInstanceQuery.orderByProcessInstanceEndTime().list();
		return ProcessDtoConvertor.convertHisProcessToDto(list);
	}

	@Override
	public Long getHistoryProcessInstanceCount(ProcessQueryDto queryDto) {
		logger.info("查询历史流程总数：username: " + queryDto.getUserName());
		HistoryService historyService = processEngine.getHistoryService();
		// 1. 创建历史流程实例，查询对象
		HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery();
		// 2. 设置指定用户参与的流程信息
		historicProcessInstanceQuery.involvedUser(queryDto.getUserName());
		return historicProcessInstanceQuery.count();
	}

	@Override
	public Map<String, String> getProcBizKeyMapById(Set<String> procInstIds) {
		Map<String, String> idToBizkeyMap = new HashMap<String, String>();
		HistoryService historyService = processEngine.getHistoryService();
		// 1. 创建历史流程实例，查询对象
		HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery();
		List<HistoricProcessInstance> procList = historicProcessInstanceQuery.processInstanceIds(procInstIds).list();
		if (procList != null) {
			for (HistoricProcessInstance procInst : procList) {
				idToBizkeyMap.put(procInst.getId(), procInst.getBusinessKey());
			}
		}
		return idToBizkeyMap;
	}

	@Override
	public void hungUpProcessInstanceByBizKey(String businessKey) {
		logger.info("挂起流程：" + businessKey);
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
		        .processInstanceBusinessKey(businessKey).singleResult();
		if (processInstance != null) {
			runtimeService.suspendProcessInstanceById(processInstance.getId());
		} else {
			throw new RuntimeException("未查询到对应的流程实例！");
		}
	}
}
