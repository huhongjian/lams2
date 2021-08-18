package com.bupt.lams.mapper;

import com.bupt.lams.model.planItem.Advisory;

import java.util.List;

public interface AdvisoryMapper {
    int insertSelective(Advisory advisory);

    List<Advisory> getAdvisoryByIds(List<Long> ids);

    Advisory getById(Long id);

    void update(Advisory advisory);

    void clear(String userEmail);
}
