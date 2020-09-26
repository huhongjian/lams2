package com.bupt.lams.service;

import com.bupt.lams.mapper.AssetWorkflowMapper;
import com.bupt.lams.model.AssetWorkflow;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AssetWorkflowService {
    @Resource
    AssetWorkflowMapper assetWorkflowMapper;

    public AssetWorkflow getAssetWorkflowByAid(Long aid) {
        return assetWorkflowMapper.getAssetWorkflowByAid(aid);
    }

    public void saveAssetWorkflow(AssetWorkflow assetWorkflow) {
        assetWorkflowMapper.insertSelective(assetWorkflow);
    }
}
