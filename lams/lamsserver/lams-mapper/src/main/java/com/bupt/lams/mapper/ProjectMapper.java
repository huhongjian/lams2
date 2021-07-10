package com.bupt.lams.mapper;

import com.bupt.lams.dto.AssetQueryCondition;
import com.bupt.lams.dto.ProjectQueryCondition;
import com.bupt.lams.model.Project;

import java.util.List;

public interface ProjectMapper {
    /**
     * 根据条件获取科研项目信息
     *
     * @param condition
     * @return
     */
    List<Project> getProjectByCondition(ProjectQueryCondition condition);

    /**
     * 获取符合条件的资产总数
     *
     * @param condition
     * @return
     */
    Long getTotalByCondition(AssetQueryCondition condition);

    int insertSelective(Project project);

    void updateProject(Project project);
}
