package com.bupt.lams.service;

import com.bupt.lams.mapper.OperateTypeWorkflowMapper;
import com.bupt.lams.model.OperateTypeWorkflow;
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
        OperateTypeWorkflow operateTypeWorkflow = operateTypeWorkflowMapper.selectByOperateType(operateType);
        if (operateTypeWorkflow != null) {
            return operateTypeWorkflow.getWorkflowKey();
        }
        return null;
    }
}
