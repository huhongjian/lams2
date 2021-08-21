package com.bupt.lams.mapper;

import com.bupt.lams.model.planItem.Advisory;

import java.util.List;

public interface AdvisoryMapper {
    int insertSelective(Advisory advisory);

    List<Advisory> getAdvisoryByIds(List<Long> ids);

    /**
     * 根据ids获取完整信息，用于执行预算明细
     */
    List<Advisory> getFullInfoByIds(List<Long> ids);

    Advisory getById(Long id);

    void update(Advisory advisory);

    void clear(String userEmail);

    List<Advisory> getAll();
}
