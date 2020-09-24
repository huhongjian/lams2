package com.bupt.lams.service;

import com.bupt.lams.constants.OperateTypeEnum;
import com.bupt.lams.constants.ProcessTypeEnum;
import com.bupt.lams.constants.WorkflowConstant;
import com.bupt.lams.mapper.AssetMapper;
import com.bupt.lams.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资产service
 */
@Service
public class AssetService {
    private Logger logger = LoggerFactory.getLogger(AssetService.class);

    @Resource
    AssetMapper assetMapper;
    @Resource
    TaskOperateService taskOperateService;

    public RespPageBean getAssetByPage(Integer page, Integer size, Asset asset, Date[] beginDateScope) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Asset> data = assetMapper.getAssetByPage(page, size, asset, beginDateScope);
        Long total = assetMapper.getTotal(asset, beginDateScope);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer addAssetIn(Asset asset) {
        int result = assetMapper.insertSelective(asset);
        // 构造record
        Record record = new Record();
        record.setAid(asset.getId());
        record.setType(ProcessTypeEnum.IN.getIndex());
        record.setOperate(OperateTypeEnum.CREATE.getName());
        record.setOperateType(OperateTypeEnum.CREATE.getIndex());
        record.setOperator(asset.getApplicant());
        record.setOperatorMail(asset.getApplicantEmail());
        record.setOperateTime(asset.getApplyDate());
        try {
            taskOperateService.startWorkFlow(record, getDefaultStartWorkFlowParamsMap());
        } catch (Exception e) {
            logger.error("启动工作流失败", e);
            e.printStackTrace();
            throw e;
        }
        return result;
    }

    public Asset selectByPrimaryKey(Long id) {
        return assetMapper.selectByPrimaryKey(id);
    }

    /**
     * 初始化流程启动参数
     *
     * @return 启动参数Map
     */
    private Map<String, String> getDefaultStartWorkFlowParamsMap() {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(WorkflowConstant.WORKFLOW_PARAM_KEY_FIRST_ASSIGNEE, null);
        paramsMap.put(WorkflowConstant.WORKFLOW_PARAM_KEY_RESPONSE_TO_ASSIGNEE, null);
        return paramsMap;
    }
}
