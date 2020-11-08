package com.bupt.lams.controller.purchase;

import com.bupt.lams.dto.AddPurchaseData;
import com.bupt.lams.dto.PurchaseQueryCondition;
import com.bupt.lams.model.PurchaseOrder;
import com.bupt.lams.model.PurchasePic;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.model.RespPageBean;
import com.bupt.lams.service.PurchaseOrderService;
import com.bupt.lams.utils.FastDFSUtils;
import com.bupt.lams.utils.UserInfoUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @Value("${fastdfs.nginx.host}")
    String nginxHost;

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
            List<Long> aids = addData.getAids();
            List<Long> poids = purchaseOrderService.getPurchaseOrderIdsByAids(aids);
            if (CollectionUtils.isNotEmpty(poids)) {
                return RespBean.error("订单信息已存在，不能重复添加！");
            }
            purchaseOrderService.addPurchaseOrder(addData);
            response.setObj(addData.getPurchaseOrder().getId());
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

    @PutMapping("/edit")
    public RespBean updatePurchaseOrder(@RequestBody PurchaseOrder purchaseOrder) {
        // 订单信息只允许财务修改
        if (UserInfoUtils.isAccountant() == false) {
            return RespBean.error("没有修改权限，请联系财务人员修改!");
        }
        try {
            purchaseOrderService.updatePurchaseOrder(purchaseOrder);
        } catch (Exception e) {
            return RespBean.error("更新失败!");
        }
        return RespBean.ok("更新成功!");
    }

    @PostMapping("/pic/add")
    public RespBean addPics(MultipartFile file, Long poid) {
        RespBean response = new RespBean();
        response.setStatus(200);
        response.setMsg("更新订单图片成功!");
        try {
            String fileId = FastDFSUtils.upload(file);
            String url = nginxHost + fileId;
            PurchasePic pic = new PurchasePic(poid, file.getOriginalFilename(), url, new Date());
            purchaseOrderService.insertPurchasePics(pic);
            return response;
        } catch (Exception e) {
            logger.error("更新订单图片失败！", e);
            return RespBean.error("更新订单图片失败!");
        }
    }

    @DeleteMapping("/pic/remove")
    public RespBean removeAssetPics(Long pid) {
        RespBean response = new RespBean();
        response.setStatus(200);
        response.setMsg("删除订单图片成功!");
        try {
            purchaseOrderService.deletePurchasePicById(pid);
            return response;
        } catch (Exception e) {
            logger.error("删除订单图片失败！", e);
            return RespBean.error("删除订单图片失败!");
        }
    }
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
}
