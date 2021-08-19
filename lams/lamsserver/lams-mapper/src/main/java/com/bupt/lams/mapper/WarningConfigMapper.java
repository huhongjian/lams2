package com.bupt.lams.mapper;

import com.bupt.lams.dto.WarningConfigQueryCondition;
import com.bupt.lams.model.WarningConfig;

import java.util.List;

public interface WarningConfigMapper {
    /**
     * 根据条件获取执行报警信息
     *
     * @param condition
     * @return
     */
    List<WarningConfig> getWarningConfigByCondition(WarningConfigQueryCondition condition);

    int insertSelective(WarningConfig warningConfig);

    void update(WarningConfig warningConfig);

    void deleteConfigs(List<Long> ids);
}
