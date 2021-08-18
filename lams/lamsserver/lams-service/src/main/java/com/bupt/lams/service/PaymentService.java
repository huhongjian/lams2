package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.mapper.PaymentMapper;
import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.PlanAndItem;
import com.bupt.lams.model.planItem.Payment;
import com.bupt.lams.utils.ComputeUtils;
import com.bupt.lams.utils.TransferUtils;
import com.bupt.lams.utils.UserInfoUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试化验加工费service
 */
@Service
public class PaymentService {
    @Resource
    PaymentMapper paymentMapper;

    @Transactional(rollbackFor = Exception.class)
    public Long addPayment(Payment payment) {
        payment.setItemType(PlanItemTypeEnum.PAYMENT.getIndex());
        payment.setUserEmail(UserInfoUtils.getLoginedUser().getUsername());
        // 计算合计
        Double total = payment.getStandard() * payment.getAmount() * payment.getHours() / 10000D;
        payment.setSubTotal(total);
        payment.setUnfulfilledQuota(payment.getSubTotal());
        payment.setExecuteRate(ComputeUtils.computeExecuteRate(payment.getSubTotal(), payment.getUnfulfilledQuota()));
        paymentMapper.insertSelective(payment);
        return payment.getId();
    }

    public PlanItemData getDataByPlanAndItem(List<PlanAndItem> planAndItemList) {
        PlanItemData data = new PlanItemData();
        List<Long> idList = new ArrayList<>();
        if (planAndItemList == null || planAndItemList.size() == 0) {
            return data;
        }
        for (PlanAndItem item : planAndItemList) {
            if (item.getItid() == PlanItemTypeEnum.PAYMENT.getIndex()) {
                idList.add(item.getIid());
            }
        }
        if (idList.size() == 0) {
            return data;
        }
        data = getPaymentFeeDataByIds(idList);
        return data;
    }

    public PlanItemData getPaymentFeeDataByIds(List<Long> ids) {
        List<Payment> equipmentFeeList = paymentMapper.getPaymentByIds(ids);
        return TransferUtils.transToPlanItemData(new ArrayList<>(equipmentFeeList));
    }

    public List<Payment> getPaymentListByIds(List<Long> pids) {
        return paymentMapper.getPaymentByIds(pids);
    }

    public void update(Payment equipmentFee) {
        paymentMapper.update(equipmentFee);
    }

    public Payment getById(Long id) {
        return paymentMapper.getById(id);
    }

    /**
     * 清空本人创建的、多余的预算明细信息，即，没有和预算计划关联的预算明细信息
     */
    public void clear() {
        LamsUser user = UserInfoUtils.getLoginedUser();
        paymentMapper.clear(user.getUsername());
    }
}
