package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.mapper.BudgetRecordMapper;
import com.bupt.lams.mapper.PublishFeeMapper;
import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.PlanAndItem;
import com.bupt.lams.model.planItem.PublishFee;
import com.bupt.lams.utils.ComputeUtils;
import com.bupt.lams.utils.TransferUtils;
import com.bupt.lams.utils.UserInfoUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 出版/文献/信息传播/知识产权事务费service
 */
@Service
public class PublishFeeService {
    @Resource
    PublishFeeMapper publishFeeMapper;
    @Resource
    BudgetRecordMapper budgetRecordMapper;

    @Transactional(rollbackFor = Exception.class)
    public Long addPublishFee(PublishFee publishFee) {
        publishFee.setItemType(PlanItemTypeEnum.PUBLISH.getIndex());
        publishFee.setUserEmail(UserInfoUtils.getLoginedUser().getUsername());
        publishFee.setUnfulfilledQuota(publishFee.getSubTotal());
        publishFee.setExecuteRate(ComputeUtils.computeExecuteRate(publishFee.getSubTotal(), publishFee.getUnfulfilledQuota()));
        publishFeeMapper.insertSelective(publishFee);
        return publishFee.getId();
    }

    public PlanItemData getDataByPlanAndItem(List<PlanAndItem> planAndItemList) {
        PlanItemData data = new PlanItemData();
        List<Long> idList = new ArrayList<>();
        if (planAndItemList == null || planAndItemList.size() == 0) {
            return data;
        }
        for (PlanAndItem item : planAndItemList) {
            if (item.getItid() == PlanItemTypeEnum.PUBLISH.getIndex()) {
                idList.add(item.getIid());
            }
        }
        if (idList.size() == 0) {
            return data;
        }
        List<PublishFee> publishFeeList = publishFeeMapper.getFullInfoByIds(idList);
        data = TransferUtils.transToPlanItemData(new ArrayList<>(publishFeeList));
        return data;
    }

    public PlanItemData getPublishFeeDataByIds(List<Long> ids) {
        List<PublishFee> publishFeeList = publishFeeMapper.getPublishFeeByIds(ids);
        return TransferUtils.transToPlanItemData(new ArrayList<>(publishFeeList));
    }

    public List<PublishFee> getPublishFeeListByIds(List<Long> pids) {
        return publishFeeMapper.getPublishFeeByIds(pids);
    }

    public void update(PublishFee publishFee) {
        publishFeeMapper.update(publishFee);
    }

    public PublishFee getById(Long id) {
        return publishFeeMapper.getById(id);
    }

    /**
     * 清空本人创建的、多余的预算明细信息，即，没有和预算计划关联的预算明细信息
     */
    public void clear() {
        LamsUser user = UserInfoUtils.getLoginedUser();
        publishFeeMapper.clear(user.getUsername());
    }

    public List<PublishFee> getRecommendFee() {
        // 查询所有设备费信息
        List<PublishFee> publishFeeList = publishFeeMapper.getAll();
        if (CollectionUtils.isEmpty(publishFeeList)) {
            return new ArrayList<>();
        }
        // 获取最近一条设备费执行记录
        Long id = budgetRecordMapper.getRecentIdByItemType(PlanItemTypeEnum.PUBLISH.getIndex());
        if (id == null) {
            return publishFeeList.size() > 10 ? publishFeeList.subList(0, 10) : publishFeeList;
        }
        PublishFee fee = publishFeeMapper.getById(id);
        if (fee == null) {
            return publishFeeList.size() > 10 ? publishFeeList.subList(0, 10) : publishFeeList;
        }
        // 计算相似度，并按照相似度逆序排列
        for (PublishFee e : publishFeeList) {
            e.computeSimilar(fee);
        }
        Collections.sort(publishFeeList, Comparator.comparingDouble(x -> -x.getSimilar()));
        return publishFeeList.size() > 10 ? publishFeeList.subList(0, 10) : publishFeeList;
    }
}
