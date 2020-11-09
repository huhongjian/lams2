package com.bupt.lams.service;

import com.bupt.lams.dto.AddPurchaseData;
import com.bupt.lams.dto.PurchaseQueryCondition;
import com.bupt.lams.mapper.PurchaseAssetMapper;
import com.bupt.lams.mapper.PurchaseOrderMapper;
import com.bupt.lams.mapper.PurchasePicsMapper;
import com.bupt.lams.model.*;
import com.bupt.lams.service.annotation.OperateRecord;
import com.bupt.lams.service.strategies.record.DeleteOrderRecord;
import com.bupt.lams.utils.FastDFSUtils;
import com.bupt.lams.utils.UserInfoUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.csource.common.MyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class PurchaseOrderService {
    @Resource
    PurchaseOrderMapper purchaseOrderMapper;
    @Resource
    PurchaseAssetMapper purchaseAssetMapper;
    @Resource
    PurchasePicsMapper purchasePicsMapper;
    @Value("${fastdfs.nginx.host}")
    String nginxHost;

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

    @Transactional(rollbackFor = Exception.class)
    public void updatePurchaseAsset(Long poid, List<Long> selectedAids) {
        purchaseAssetMapper.deleteByPoid(poid);
        if (CollectionUtils.isNotEmpty(selectedAids)) {
            purchaseAssetMapper.insertSelective(poid, selectedAids);
        }
    }

    public void updatePurchaseOrder(PurchaseOrder purchaseOrder) {
        purchaseOrderMapper.updatePurchaseOrder(purchaseOrder);
    }

    public void insertPurchasePics(PurchasePic pic) {
        purchasePicsMapper.insertPurchasePics(pic);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deletePurchasePicById(Long pid) throws IOException, MyException {
        PurchasePic pic = purchasePicsMapper.selectByPrimaryKey(pid);
        if (pic == null) {
            return;
        }
        String fileId = pic.getUrl().replace(nginxHost, "");
        FastDFSUtils.delete(fileId);
        purchasePicsMapper.deletePurchasePicById(pid);
    }

    public List<Long> getPurchaseOrderIdsByAids(List<Long> aids) {
        return purchaseAssetMapper.getPurchaseOrderIdsByAids(aids);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deletePurchaseOrders(List<Long> poids) {
        purchaseOrderMapper.deleteManyByIds(poids);
        purchaseAssetMapper.deleteManyByIds(poids);
    }
}
