package com.bupt.lams.mapper;

import com.bupt.lams.model.ProcessTypeWorkflow;

public interface OperateTypeWorkflowMapper {
    ProcessTypeWorkflow selectByOperateType(Integer operateType);
}
