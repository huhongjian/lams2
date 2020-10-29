package com.bupt.lams.mapper;

import com.bupt.lams.model.ProcessTypeWorkflow;

public interface ProcessWorkflowMapper {
    ProcessTypeWorkflow selectByCategory(Integer category);
}
