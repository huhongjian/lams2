package com.bupt.lams.service;

import com.bupt.lams.dto.AddPurchaseData;
import com.bupt.lams.dto.PurchaseQueryCondition;
import com.bupt.lams.mapper.PurchaseAssetMapper;
import com.bupt.lams.mapper.PurchaseOrderMapper;
import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.PurchaseOrder;
import com.bupt.lams.model.RespPageBean;
import com.bupt.lams.utils.UserInfoUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class PurchaseOrderService {
    @Resource
    PurchaseOrderMapper purchaseOrderMapper;
    @Resource
    PurchaseAssetMapper purchaseAssetMapper;

    public RespPageBean getPurchaseOrdersByCondition(PurchaseQueryCondition condition) {
        Integer page = condition.getPage();
        Integer size = condition.getSize();
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        condition.setPage(page);
        List<PurchaseOrder> data = purchaseOrderMapper.getPurchaseOrderByCondition(condition);
        Long total = purchaseOrderMapper.getTotalByCondition(condition);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }

    @Transactional(rollbackFor = Exception.class)
    public void addPurchaseOrder(AddPurchaseData addData) {
        PurchaseOrder purchaseOrder = addData.getPurchaseOrder();
        List<Long> aids = addData.getAids();
        LamsUser user = UserInfoUtils.getLoginedUser();
        purchaseOrder.setCreatorEmail(user.getUsername());
        purchaseOrder.setCreateTime(new Date());
        if (purchaseOrder == null) {
            return;
        }
        purchaseOrderMapper.insertSelective(purchaseOrder);
        if (CollectionUtils.isNotEmpty(aids)) {
            purchaseAssetMapper.insertSelective(purchaseOrder.getId(), aids);
        }
    }
}