package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.mapper.AcademicFeeMapper;
import com.bupt.lams.mapper.BudgetRecordMapper;
import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.PlanAndItem;
import com.bupt.lams.model.planItem.AcademicFee;
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
 * 国内学术会议差旅费service
 */
@Service
public class AcademicFeeService {
    @Resource
    AcademicFeeMapper academicFeeMapper;
    @Resource
    BudgetRecordMapper budgetRecordMapper;

    @Transactional(rollbackFor = Exception.class)
    public Long addAcademicFee(AcademicFee academicFee) {
        academicFee.setItemType(PlanItemTypeEnum.ACADEMIC.getIndex());
        academicFee.setUserEmail(UserInfoUtils.getLoginedUser().getUsername());
        // 计算合计
        if (academicFee.getSubTotal() == null) {
            Double total = ((academicFee.getRegistrationFee() + academicFee.getTransFee()) * academicFee.getTimes() + academicFee.getAccommodationFee() * academicFee.getDays() * academicFee.getTimes()) / 10000D;
            academicFee.setSubTotal(total);
        }
        academicFee.setSubTotal(academicFee.getSubTotal() * academicFee.getTimes());
        academicFee.setUnfulfilledQuota(academicFee.getSubTotal());
        academicFee.setExecuteRate(ComputeUtils.computeExecuteRate(academicFee.getSubTotal(), academicFee.getUnfulfilledQuota()));
        academicFeeMapper.insertSelective(academicFee);
        return academicFee.getId();
    }

    public PlanItemData getDataByPlanAndItem(List<PlanAndItem> planAndItemList) {
        PlanItemData data = new PlanItemData();
        List<Long> idList = new ArrayList<>();
        if (planAndItemList == null || planAndItemList.size() == 0) {
            return data;
        }
        for (PlanAndItem item : planAndItemList) {
            if (item.getItid() == PlanItemTypeEnum.ACADEMIC.getIndex()) {
                idList.add(item.getIid());
            }
        }
        if (idList.size() == 0) {
            return data;
        }
        List<AcademicFee> academicFeeList = academicFeeMapper.getFullInfoByIds(idList);
        data = TransferUtils.transToPlanItemData(new ArrayList<>(academicFeeList));
        return data;
    }

    public PlanItemData getAcademicFeeListByIds(List<Long> pids) {
        List<AcademicFee> academicFeeList = academicFeeMapper.getByIds(pids);
        return TransferUtils.transToPlanItemData(new ArrayList<>(academicFeeList));
    }

    public void update(AcademicFee academicFee) {
        academicFeeMapper.update(academicFee);
    }

    public AcademicFee getById(Long id) {
        return academicFeeMapper.getById(id);
    }

    /**
     * 清空本人创建的、多余的预算明细信息，即，没有和预算计划关联的预算明细信息
     */
    public void clear() {
        LamsUser user = UserInfoUtils.getLoginedUser();
        academicFeeMapper.clear(user.getUsername());
    }

    public List<AcademicFee> getRecommendFee() {
        // 查询所有设备费信息
        List<AcademicFee> academicFeeList = academicFeeMapper.getAll();
        if (CollectionUtils.isEmpty(academicFeeList)) {
            return new ArrayList<>();
        }
        // 获取最近一条设备费执行记录
        Long id = budgetRecordMapper.getRecentIdByItemType(PlanItemTypeEnum.ACADEMIC.getIndex());
        if (id == null) {
            return academicFeeList.size() > 10 ? academicFeeList.subList(0, 10) : academicFeeList;
        }
        AcademicFee fee = academicFeeMapper.getById(id);
        if (fee == null) {
            return academicFeeList.size() > 10 ? academicFeeList.subList(0, 10) : academicFeeList;
        }
        // 计算相似度，并按照相似度逆序排列
        for (AcademicFee e : academicFeeList) {
            e.computeSimilar(fee);
        }
        Collections.sort(academicFeeList, Comparator.comparingDouble(x -> -x.getSimilar()));
        return academicFeeList.size() > 10 ? academicFeeList.subList(0, 10) : academicFeeList;
    }
}
