package com.bupt.lams.utils;

import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.model.planItem.PlanItem;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class TransferUtils {
    public static PlanItemData transToPlanItemData(List<PlanItem> planItemList){
        PlanItemData data = new PlanItemData();
        if (CollectionUtils.isEmpty(planItemList)) {
            return data;
        }
        Double total = new Double(0.0);
        Double unfulfilledQuota = new Double(0.0);
        for (PlanItem fee : planItemList) {
            total = total + fee.getSubTotal();
            unfulfilledQuota = unfulfilledQuota + fee.getUnfulfilledQuota();
        }
        data.setFunding(total);
        data.setUnfulfilledQuota(unfulfilledQuota);
        data.setExecuteRate(ComputeUtils.computeExecuteRate(total, unfulfilledQuota));
        data.setPlanItemList(new ArrayList<>(planItemList));
        return data;
    }
}
