package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.mapper.ResearchFeeMapper;
import com.bupt.lams.model.PlanAndItem;
import com.bupt.lams.model.planItem.PublishFee;
import com.bupt.lams.model.planItem.ResearchFee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 课题调研差旅费service
 */
@Service
public class ResearchFeeService {
    @Resource
    ResearchFeeMapper researchFeeMapper;

    public List<ResearchFee> getResearchFeeList() {
        return researchFeeMapper.getResearchFeeList();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long addResearchFee(ResearchFee researchFee) {
        researchFee.setItemType(PlanItemTypeEnum.RESEARCH.getIndex());
        // 计算合计
        if (researchFee.getSubTotal() == null) {
            researchFee.setSubTotal(0.0);
        }
        researchFeeMapper.insertSelective(researchFee);
        return researchFee.getId();
    }

    public List<ResearchFee> getResearchFeeByPlanAndItem(List<PlanAndItem> planAndItemList) {
        List<ResearchFee> res = new ArrayList<>();
        List<Long> idList = new ArrayList<>();
        if (planAndItemList == null || planAndItemList.size() == 0) {
            return res;
        }
        for (PlanAndItem item : planAndItemList) {
            if (item.getItid() == PlanItemTypeEnum.RESEARCH.getIndex()) {
                idList.add(item.getIid());
            }
        }
        if (idList.size() == 0) {
            return res;
        }
        return researchFeeMapper.getResearchFeeByIds(idList);
    }

    public List<ResearchFee> getResearchFeeListByIds(List<Long> pids) {
        return researchFeeMapper.getResearchFeeByIds(pids);
    }
}
