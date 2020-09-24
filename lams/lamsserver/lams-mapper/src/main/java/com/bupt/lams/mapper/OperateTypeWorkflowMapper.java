package com.bupt.lams.mapper;

import com.bupt.lams.model.OperateTypeWorkflow;

public interface OperateTypeWorkflowMapper {
    OperateTypeWorkflow selectByOperateType(Integer operateType);
}
