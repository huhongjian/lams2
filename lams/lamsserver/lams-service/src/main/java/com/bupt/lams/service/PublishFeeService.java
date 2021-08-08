package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.mapper.PublishFeeMapper;
import com.bupt.lams.model.planItem.PublishFee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 测试化验加工费service
 */
@Service
public class PublishFeeService {
    @Resource
    PublishFeeMapper publishFeeMapper;

    public List<PublishFee> getPublishFeeList() {
        return publishFeeMapper.getPublishFeeList();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long addPublishFee(PublishFee publishFee) {
        publishFee.setItemType(PlanItemTypeEnum.PUBLISH.getIndex());
        // 计算合计
        if (publishFee.getSubTotal() == null) {
            publishFee.setSubTotal(0.0);
        }
        publishFeeMapper.insertSelective(publishFee);
        return publishFee.getId();
    }
}
