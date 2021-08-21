package com.bupt.lams.mapper;

import com.bupt.lams.model.planItem.ResearchFee;

import java.util.List;

public interface ResearchFeeMapper {
    int insertSelective(ResearchFee researchFee);

    List<ResearchFee> getResearchFeeByIds(List<Long> ids);

    /**
     * 根据ids获取完整信息，用于执行预算明细
     */
    List<ResearchFee> getFullInfoByIds(List<Long> ids);

    ResearchFee getById(Long id);

    void update(ResearchFee researchFee);

    void clear(String userEmail);

    List<ResearchFee> getAll();
}
