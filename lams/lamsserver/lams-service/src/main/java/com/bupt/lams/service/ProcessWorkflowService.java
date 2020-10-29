package com.bupt.lams.service;

import com.bupt.lams.mapper.ProcessWorkflowMapper;
import com.bupt.lams.model.ProcessTypeWorkflow;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ProcessWorkflowService {
    @Resource
    ProcessWorkflowMapper processWorkflowMapper;

    /**
     * 根据操作类型获取工作流key
     *
     * @param category
     * @return
     */
    public String selectWorkflowKeyByCategory(Integer category) {
        ProcessTypeWorkflow processTypeWorkflow = processWorkflowMapper.selectByCategory(category);
        if (processTypeWorkflow != null) {
            return processTypeWorkflow.getWorkflowKey();
        }
        return null;
    }
}
