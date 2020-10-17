package com.bupt.lams.service;

import com.bupt.lams.mapper.OperateTypeWorkflowMapper;
import com.bupt.lams.model.ProcessTypeWorkflow;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OperateTypeWorkflowService {
    @Resource
    OperateTypeWorkflowMapper operateTypeWorkflowMapper;

    /**
     * 根据操作类型获取工作流key
     *
     * @param operateType
     * @return
     */
    public String selectWorkflowKeyByOperateType(Integer operateType) {
        ProcessTypeWorkflow processTypeWorkflow = operateTypeWorkflowMapper.selectByOperateType(operateType);
        if (processTypeWorkflow != null) {
            return processTypeWorkflow.getWorkflowKey();
        }
        return null;
    }
}
