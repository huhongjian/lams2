package com.bupt.lams.mapper;

import com.bupt.lams.model.planItem.AcademicFee;

import java.util.List;

public interface AcademicFeeMapper {
    int insertSelective(AcademicFee academicFee);

    List<AcademicFee> getAcademicFeeByIds(List<Long> ids);

    AcademicFee getById(Long id);

    void update(AcademicFee academicFee);

    void clear(String userEmail);
}
