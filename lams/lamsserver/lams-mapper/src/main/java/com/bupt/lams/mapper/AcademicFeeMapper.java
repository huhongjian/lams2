package com.bupt.lams.mapper;

import com.bupt.lams.model.planItem.AcademicFee;
import com.bupt.lams.model.planItem.MeetingFee;

import java.util.List;

public interface AcademicFeeMapper {

    List<AcademicFee> getAcademicFeeList();

    int insertSelective(AcademicFee academicFee);
}
