package com.bupt.lams.service;

import com.bupt.lams.mapper.ProjectTypeMapper;
import com.bupt.lams.model.AssetType;
import com.bupt.lams.model.ProjectType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 项目类型service
 */
@Service
public class ProjectTypesService {
    @Resource
    ProjectTypeMapper projectTypeMapper;

    public List<ProjectType> getAllProjectTypes() {
        return projectTypeMapper.getAllProjectTypes();
    }
}
