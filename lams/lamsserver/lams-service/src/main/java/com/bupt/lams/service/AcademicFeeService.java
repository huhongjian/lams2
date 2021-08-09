package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.mapper.AcademicFeeMapper;
import com.bupt.lams.model.planItem.AcademicFee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
}
