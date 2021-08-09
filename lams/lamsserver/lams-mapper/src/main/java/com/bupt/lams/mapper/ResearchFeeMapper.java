package com.bupt.lams.mapper;

import com.bupt.lams.model.planItem.ResearchFee;

import java.util.List;

public interface ResearchFeeMapper {

    List<ResearchFee> getResearchFeeList();

    int insertSelective(ResearchFee researchFee);
}
