package com.bupt.lams.mapper;

import com.bupt.lams.model.planItem.AcademicFee;

import java.util.List;

public interface AcademicFeeMapper {
    int insertSelective(AcademicFee academicFee);

    /**
     * 根据ids获取基本信息，用于新建预算计划明细，那时和预算计划以及科研项目还没有关联关系
     */
    List<AcademicFee> getByIds(List<Long> ids);

    /**
     * 根据ids获取完整信息，用于执行预算明细
     */
    List<AcademicFee> getFullInfoByIds(List<Long> ids);

    AcademicFee getById(Long id);

    void update(AcademicFee academicFee);

    void clear(String userEmail);

    List<AcademicFee> getAll();
}
