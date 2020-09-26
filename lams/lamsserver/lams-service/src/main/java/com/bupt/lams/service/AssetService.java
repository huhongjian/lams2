package com.bupt.lams.service;

import com.bupt.lams.constants.AssetStatusEnum;
import com.bupt.lams.constants.OperateTypeEnum;
import com.bupt.lams.constants.ProcessTypeEnum;
import com.bupt.lams.constants.WorkflowConstant;
import com.bupt.lams.mapper.AssetMapper;
import com.bupt.lams.model.Asset;
import com.bupt.lams.model.Hr;
import com.bupt.lams.model.Record;
import com.bupt.lams.model.RespPageBean;
import com.bupt.lams.utils.UserInfoUtils;
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
        asset.setCategory(ProcessTypeEnum.IN.getIndex());
        asset.setChargerByApplicant();
        asset.setStatus(AssetStatusEnum.CREATE.getName());
        asset.setApplyDate(new Date());
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
            taskOperateService.startWorkFlow(record, null);
        } catch (Exception e) {
            logger.error("启动工作流失败", e);
            throw e;
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void borrowAsset(Asset asset) {
        Hr user = UserInfoUtils.getLoginedUser();
        asset.setCharger(user.getName());
        asset.setStatus(AssetStatusEnum.ASK.getName());
        asset.setApplyDate(new Date());
        assetMapper.updateAsset(asset);
        // 构造record
        Record record = new Record();
        record.setAid(asset.getId());
        record.setType(ProcessTypeEnum.OUT.getIndex());
        record.setOperate(OperateTypeEnum.BORROW.getName());
        record.setOperateType(OperateTypeEnum.BORROW.getIndex());
        record.setOperator(asset.getCharger());
        record.setOperatorMail(asset.getChargerEmail());
        record.setOperateTime(asset.getApplyDate());
        Map<String, String> variablesMap = new HashMap<>();
        variablesMap.put(WorkflowConstant.NEXT_USER, record.getOperator());
        try {
            taskOperateService.startWorkFlow(record, variablesMap);
        } catch (Exception e) {
            logger.error("启动工作流失败", e);
            throw e;
        }
    }

    public Asset selectByPrimaryKey(Long id) {
        return assetMapper.selectByPrimaryKey(id);
    }
}
