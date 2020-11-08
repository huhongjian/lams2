package com.bupt.lams.controller.purchase;

import com.bupt.lams.constants.AssetStatusEnum;
import com.bupt.lams.constants.OrderStatusEnum;
import com.bupt.lams.dto.AddPurchaseData;
import com.bupt.lams.dto.AssetQueryCondition;
import com.bupt.lams.dto.PurchaseQueryCondition;
import com.bupt.lams.model.*;
import com.bupt.lams.service.OrderAssetService;
import com.bupt.lams.service.PurchaseOrderService;
import com.bupt.lams.utils.POIUtils;
import com.bupt.lams.utils.UserInfoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 订单信息相关
 */
@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    private Logger logger = LoggerFactory.getLogger(PurchaseController.class);

    @Resource
    PurchaseOrderService purchaseOrderService;
    @Resource
    OrderAssetService orderAssetService;

    @GetMapping("/get")
    public RespPageBean getPurchaseOrderByPage(PurchaseQueryCondition condition, Date[] purchaseDateScope, Date[] invoiceDateScope) {
        if (purchaseDateScope != null && purchaseDateScope.length == 2) {
            condition.setStartDateForPurchase(purchaseDateScope[0]);
            condition.setEndDateForPurchase(purchaseDateScope[1]);
        }
        if (invoiceDateScope != null && invoiceDateScope.length == 2) {
            condition.setStartDateForInvoice(invoiceDateScope[0]);
            condition.setEndDateForInvoice(invoiceDateScope[1]);
        }
        return purchaseOrderService.getPurchaseOrdersByCondition(condition);
    }

    @PostMapping("/add")
    public RespBean addPurchaseOrder(@RequestBody AddPurchaseData addData) {
        RespBean response = new RespBean();
        response.setStatus(200);
        response.setMsg("订单信息添加成功!");
        try {
            purchaseOrderService.addPurchaseOrder(addData);
        } catch (Exception e) {
            logger.error("订单信息添加失败", e);
            return RespBean.error("订单信息添加失败！");
        }
        return response;
    }

    @PutMapping("/asset")
    public RespBean updatePurchaseAsset(Long poid, @RequestBody List<Long> aids) {
        try {
            purchaseOrderService.updatePurchaseAsset(poid, aids);
        } catch (Exception e) {
            logger.error("更新用户角色信息失败！", e);
            return RespBean.error("更新失败!");
        }
        return RespBean.ok("更新成功!");
    }

//    @PutMapping("/edit")
//    public RespBean updateAsset(@RequestBody Asset asset) {
//        // 资产信息管理中的资产信息只允许管理员修改
//        if (UserInfoUtils.isAdmin() == false) {
//            return RespBean.error("没有修改权限，请联系管理员!");
//        }
//        try {
//            purchaseOrderService.updateAsset(asset);
//        } catch (Exception e) {
//            return RespBean.error("更新失败!");
//        }
//        return RespBean.ok("更新成功!");
//    }
//
//    @GetMapping("/export")
//    public ResponseEntity<byte[]> exportData(AssetQueryCondition assetQueryCondition, Date[] dateScope) {
//        if (dateScope != null && dateScope.length == 2) {
//            assetQueryCondition.setStartDate(dateScope[0]);
//            assetQueryCondition.setEndDate(dateScope[1]);
//        }
//        List<Asset> list = (List<Asset>) purchaseOrderService.getAssetByCondition(assetQueryCondition).getData();
//        return POIUtils.assetInfo2Excel(list);
//    }
//
//    @PutMapping("/changeStatus")
//    public RespBean changeAssetStatus(@RequestBody Asset asset) {
//        // 除了报修之外，其他情况只允许管理员修改
//        if (asset.getStatus() != AssetStatusEnum.BROKEN.getIndex() && UserInfoUtils.isAdmin() == false) {
//            return RespBean.error("没有修改权限，请联系管理员!");
//        }
//        if (isInProcess(asset.getId()) == true) {
//            return RespBean.error("当前资产在流程中，请先结束流程再操作！");
//        }
//        try {
//            purchaseOrderService.changeAssetStatus(asset);
//        } catch (Exception e) {
//            return RespBean.error("更新失败!");
//        }
//        return RespBean.ok("更新成功!");
//    }
//
//    /**
//     * 判断资产是否正在一个流程中
//     *
//     * @param aid
//     * @return
//     */
//    public boolean isInProcess(Long aid) {
//        try {
//            Order order = orderAssetService.getLatestOrderByAid(aid);
//            if (order.getStatus() != OrderStatusEnum.ASK.getIndex() && order.getStatus() != OrderStatusEnum.OCCUPIED.getIndex()) {
//                return false;
//            }
//            return true;
//        } catch (Exception e) {
//            logger.error("根据资产编号获取最近工单异常！", e);
//            return true;
//        }
//    }
}
