package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.mapper.AcademicFeeMapper;
import com.bupt.lams.model.PlanAndItem;
import com.bupt.lams.model.planItem.AcademicFee;
import com.bupt.lams.model.planItem.EquipmentFee;
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

    public List<AcademicFee> getAcademicFeeList() {
        return academicFeeMapper.getAcademicFeeList();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long addAcademicFee(AcademicFee academicFee) {
        academicFee.setItemType(PlanItemTypeEnum.MEETING.getIndex());
        // 计算合计
        if (academicFee.getSubTotal() == null) {
            academicFee.setSubTotal(0.0);
        }
        academicFeeMapper.insertSelective(academicFee);
        return academicFee.getId();
    }

    public List<AcademicFee> getAcademicFeeByPlanAndItem(List<PlanAndItem> planAndItemList) {
        List<AcademicFee> res = new ArrayList<>();
        List<Long> idList = new ArrayList<>();
        if (planAndItemList == null || planAndItemList.size() == 0) {
            return res;
        }
        for (PlanAndItem item : planAndItemList) {
            if (item.getItid() == PlanItemTypeEnum.ACADEMIC.getIndex()) {
                idList.add(item.getIid());
            }
        }
        if (idList.size() == 0) {
            return res;
        }
        return academicFeeMapper.getAcademicFeeByIds(idList);
    }

    public List<AcademicFee> getAcademicFeeListByIds(List<Long> pids) {
        return academicFeeMapper.getAcademicFeeByIds(pids);
    }
}
