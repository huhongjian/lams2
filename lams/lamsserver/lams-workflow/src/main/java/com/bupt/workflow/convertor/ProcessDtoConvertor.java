package com.bupt.workflow.convertor;

import java.util.ArrayList;
import java.util.List;

import com.bupt.workflow.dto.ProcessDto;
import org.activiti.engine.history.HistoricProcessInstance;

/**
 * 流程Dto转换器
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
		List<ProcessDto> retProcessDtoList = new ArrayList<>();

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
