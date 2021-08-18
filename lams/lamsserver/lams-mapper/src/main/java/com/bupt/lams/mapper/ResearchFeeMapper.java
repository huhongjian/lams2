package com.bupt.lams.mapper;

import com.bupt.lams.model.planItem.ResearchFee;

import java.util.List;

public interface ResearchFeeMapper {
    int insertSelective(ResearchFee researchFee);

    List<ResearchFee> getResearchFeeByIds(List<Long> ids);

    ResearchFee getById(Long id);

    void update(ResearchFee researchFee);

    void clear(String userEmail);
}
