package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.mapper.AdvisoryMapper;
import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.PlanAndItem;
import com.bupt.lams.model.planItem.Advisory;
import com.bupt.lams.utils.ComputeUtils;
import com.bupt.lams.utils.TransferUtils;
import com.bupt.lams.utils.UserInfoUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 专家咨询费service
 */
@Service
public class AdvisoryService {
    @Resource
    AdvisoryMapper advisoryMapper;

    @Transactional(rollbackFor = Exception.class)
    public Long addAdvisory(Advisory advisory) {
        advisory.setItemType(PlanItemTypeEnum.ADVISORY.getIndex());
        advisory.setUserEmail(UserInfoUtils.getLoginedUser().getUsername());
        // 计算合计
        Double total = advisory.getStandard() * advisory.getAmount() * advisory.getDays() / 10000D;
        advisory.setSubTotal(total);
        advisory.setUnfulfilledQuota(advisory.getSubTotal());
        advisory.setExecuteRate(ComputeUtils.computeExecuteRate(advisory.getSubTotal(), advisory.getUnfulfilledQuota()));
        advisoryMapper.insertSelective(advisory);
        return advisory.getId();
    }

    public PlanItemData getDataByPlanAndItem(List<PlanAndItem> planAndItemList) {
        PlanItemData data = new PlanItemData();
        List<Long> idList = new ArrayList<>();
        if (planAndItemList == null || planAndItemList.size() == 0) {
            return data;
        }
        for (PlanAndItem item : planAndItemList) {
            if (item.getItid() == PlanItemTypeEnum.ADVISORY.getIndex()) {
                idList.add(item.getIid());
            }
        }
        if (idList.size() == 0) {
            return data;
        }
        data = getAdvisoryDataByIds(idList);
        return data;
    }

    public PlanItemData getAdvisoryDataByIds(List<Long> ids) {
        List<Advisory> advisoryList = advisoryMapper.getAdvisoryByIds(ids);
        return TransferUtils.transToPlanItemData(new ArrayList<>(advisoryList));
    }

    public List<Advisory> getAdvisoryListByIds(List<Long> ids) {
        return advisoryMapper.getAdvisoryByIds(ids);
    }

    public void update(Advisory advisory) {
        advisoryMapper.update(advisory);
    }

    public Advisory getById(Long id) {
        return advisoryMapper.getById(id);
    }

    /**
     * 清空本人创建的、多余的预算明细信息，即，没有和预算计划关联的预算明细信息
     */
    public void clear() {
        LamsUser user = UserInfoUtils.getLoginedUser();
        advisoryMapper.clear(user.getUsername());
    }
}
