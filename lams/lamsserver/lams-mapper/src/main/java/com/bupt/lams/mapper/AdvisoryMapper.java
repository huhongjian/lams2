package com.bupt.lams.mapper;

import com.bupt.lams.model.planItem.Advisory;

import java.util.List;

public interface AdvisoryMapper {

    List<Advisory> getAdvisoryList();

    int insertSelective(Advisory advisory);
}
