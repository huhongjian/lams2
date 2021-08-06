package com.bupt.lams.service;

import com.bupt.lams.mapper.PlanItemTypeMapper;
import com.bupt.lams.model.PlanItemType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 预算科目类型service
 */
@Service
public class PlanItemTypesService {
    @Resource
    PlanItemTypeMapper planItemTypeMapper;

    public List<PlanItemType> getAllPlanItemTypes() {
        return planItemTypeMapper.getAllPlanItemTypes();
    }
}
