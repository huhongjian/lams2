package com.sogou.admin.workorder.workflow.convertor;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.history.HistoricProcessInstance;

import com.sogou.admin.workorder.workflow.dto.ProcessDto;

/**
 * 流程Dto转换器
 *
 * @author shanglonghua
 */
public class ProcessDtoConvertor {

	/**
	 * Activiti对象转换业务对象
	 *
	 * @param processList
	 *            Activiti返回对象列表
	 * @return 业务对象列表
	 */
	public static List<ProcessDto> convertHisProcessToDto(List<HistoricProcessInstance> processList) {
		List<ProcessDto> retProcessDtoList = new ArrayList<ProcessDto>();

		if (processList != null && processList.size() > 0) {
			ProcessDto tmpProcessDto;
			for (HistoricProcessInstance hisProcInst : processList) {
				tmpProcessDto = new ProcessDto();
				tmpProcessDto.setBusinessKey(hisProcInst.getBusinessKey());
				tmpProcessDto.setProcInstId(hisProcInst.getId());
				tmpProcessDto.setStartTime(hisProcInst.getStartTime());
				tmpProcessDto.setEndTime(hisProcInst.getEndTime());
				retProcessDtoList.add(tmpProcessDto);
			}
		}

		return retProcessDtoList;
	}

}
