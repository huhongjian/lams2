package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.mapper.AcademicFeeMapper;
import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.PlanAndItem;
import com.bupt.lams.model.planItem.AcademicFee;
import com.bupt.lams.utils.ComputeUtils;
import com.bupt.lams.utils.TransferUtils;
import com.bupt.lams.utils.UserInfoUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 国内学术会议差旅费service
 */
@Service
public class AcademicFeeService {
    @Resource
    AcademicFeeMapper academicFeeMapper;

    @Transactional(rollbackFor = Exception.class)
    public Long addAcademicFee(AcademicFee academicFee) {
        academicFee.setItemType(PlanItemTypeEnum.ACADEMIC.getIndex());
        academicFee.setUserEmail(UserInfoUtils.getLoginedUser().getUsername());
        // 计算合计
        Double total = (academicFee.getRegistrationFee() * academicFee.getTimes() + (academicFee.getTransFee() + academicFee.getAccommodationFee()) * academicFee.getDays() * academicFee.getTimes()) / 10000D;
        academicFee.setSubTotal(total);
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
        data = getAcademicFeeListByIds(idList);
        return data;
    }

    public PlanItemData getAcademicFeeListByIds(List<Long> pids) {
        List<AcademicFee> academicFeeList = academicFeeMapper.getAcademicFeeByIds(pids);
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
}
