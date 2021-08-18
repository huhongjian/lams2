package com.bupt.lams.mapper;

import com.bupt.lams.model.planItem.ResearchFee;

import java.util.List;

public interface ResearchFeeMapper {

    List<ResearchFee> getResearchFeeList();

    int insertSelective(ResearchFee researchFee);

    List<ResearchFee> getResearchFeeByIds(List<Long> ids);

    ResearchFee getById(Long id);

    void update(ResearchFee researchFee);
}
