package com.bupt.lams.controller.asset;

import com.bupt.lams.constants.AssetStatusEnum;
import com.bupt.lams.constants.OrderStatusEnum;
import com.bupt.lams.dto.AddAssetData;
import com.bupt.lams.dto.AssetQueryCondition;
import com.bupt.lams.dto.OrderAssetData;
import com.bupt.lams.model.Asset;
import com.bupt.lams.model.Order;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.model.RespPageBean;
import com.bupt.lams.service.AssetService;
import com.bupt.lams.service.OrderAssetService;
import com.bupt.lams.utils.POIUtils;
import com.bupt.lams.utils.UserInfoUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 资产信息相关
 */
@RestController
@RequestMapping("/asset")
public class AssetController {

    private Logger logger = LoggerFactory.getLogger(AssetController.class);

    @Resource
    AssetService assetService;
    @Resource
    OrderAssetService orderAssetService;

    @GetMapping("/get")
    public RespPageBean getAssetInfoByPage(AssetQueryCondition assetQueryCondition, Date[] dateScope) {
        if (dateScope != null && dateScope.length == 2) {
            assetQueryCondition.setStartDate(dateScope[0]);
            assetQueryCondition.setEndDate(dateScope[1]);
        }
        return assetService.getAssetByCondition(assetQueryCondition);
    }

    /**
     * 获取可以借用的资产集合
     *
     * @param assetQueryCondition
     * @param dateScope
     * @return
     */
    @GetMapping("/getAvailable")
    public RespPageBean getAvailable(AssetQueryCondition assetQueryCondition, Date[] dateScope) {
        if (dateScope != null && dateScope.length == 2) {
            assetQueryCondition.setStartDate(dateScope[0]);
            assetQueryCondition.setEndDate(dateScope[1]);
        }
        return assetService.getAvailable(assetQueryCondition);
    }

    /**
     * 获取我借的资产集合
     *
     * @param assetQueryCondition
     * @param dateScope
     * @return
     */
    @GetMapping("/getReturn")
    public RespPageBean getReturn(AssetQueryCondition assetQueryCondition, Date[] dateScope) {
        if (dateScope != null && dateScope.length == 2) {
            assetQueryCondition.setStartDate(dateScope[0]);
            assetQueryCondition.setEndDate(dateScope[1]);
        }
        return assetService.getReturn(assetQueryCondition);
    }

    @GetMapping("/getCur")
    public RespPageBean getCurrentAssetInfoByPage(Integer page, Integer size) {
        return assetService.getCurrentAssetInfoByPage(page, size);
    }

    @GetMapping("/getOrderAssetList")
    public RespPageBean getOrderAssetListByPage(Long oid, Integer page, Integer size) {
        return assetService.getOrderAssetListByPage(oid, page, size);
    }

    @PostMapping("/add")
    public RespBean addAsset(@RequestBody AddAssetData addAssetData) {
        RespBean response = new RespBean();
        response.setStatus(200);
        response.setMsg("资产新增成功!");
        Asset asset = addAssetData.getAsset();
        Integer amount = addAssetData.getAmount() == null ? 1 : addAssetData.getAmount();
        if (amount <= 0 || amount >= 100) {
            logger.error("资产数量不在限定范围！");
            return RespBean.error("资产数量不在限定范围！");
        }
        try {
            List<Long> aids = assetService.addAsset(asset, amount);
            response.setObj(aids);
        } catch (Exception e) {
            logger.error("新增资产失败", e);
            return RespBean.error("新增资产失败！");
        }
        return response;
    }

    @PutMapping("/edit")
    public RespBean updateAsset(@RequestBody Asset asset) {
        // 资产信息管理中的资产信息只允许管理员修改
        if (UserInfoUtils.isAdmin() == false) {
            return RespBean.error("没有修改权限，请联系管理员!");
        }
        try {
            assetService.updateAsset(asset);
        } catch (Exception e) {
            return RespBean.error("更新失败!");
        }
        return RespBean.ok("更新成功!");
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportData(AssetQueryCondition assetQueryCondition, Date[] dateScope) {
        if (dateScope != null && dateScope.length == 2) {
            assetQueryCondition.setStartDate(dateScope[0]);
            assetQueryCondition.setEndDate(dateScope[1]);
        }
        List<Asset> list = (List<Asset>) assetService.getAssetByCondition(assetQueryCondition).getData();
        return POIUtils.assetInfo2Excel(list);
    }

    @PutMapping("/changeStatus")
    public RespBean changeAssetStatus(@RequestBody Asset asset) {
        // 除了报修之外，其他情况只允许管理员修改
        if (asset.getStatus() != AssetStatusEnum.BROKEN.getIndex() && UserInfoUtils.isAdmin() == false) {
            return RespBean.error("没有修改权限，请联系管理员!");
        }
        if (isInProcess(asset.getId()) == true) {
            return RespBean.error("当前资产在流程中，请先结束流程再操作！");
        }
        try {
            assetService.changeAssetStatus(asset);
        } catch (Exception e) {
            return RespBean.error("更新失败!");
        }
        return RespBean.ok("更新成功!");
    }

    @GetMapping("/all")
    public RespBean getAllAssetIds() {
        RespBean response = new RespBean();
        response.setStatus(200);
        response.setMsg("获取资产编号成功!");
        try {
            List<Long> aids = assetService.getAllAssetIds();
            response.setObj(aids);
        } catch (Exception e) {
            logger.error("获取资产编号失败", e);
            return RespBean.error("获取资产编号失败！");
        }
        return response;
    }

    /**
     * 判断资产是否正在一个流程中
     *
     * @param aid
     * @return
     */
    public boolean isInProcess(Long aid) {
        try {
            Order order = orderAssetService.getLatestOrderByAid(aid);
            if (order.getStatus() != OrderStatusEnum.ASK.getIndex() && order.getStatus() != OrderStatusEnum.OCCUPIED.getIndex()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.error("根据资产编号获取最近工单异常！", e);
            return true;
        }
    }

    @DeleteMapping("/delete")
    public RespBean deleteAssets(@RequestBody OrderAssetData deleteData) {
        try {
            List<Long> aids = deleteData.getAssetIds();
            Long oid = deleteData.getOid();
            assetService.deleteAssets(aids, oid);
        } catch (Exception e) {
            return RespBean.error("删除失败!");
        }
        return RespBean.ok("删除成功!");
    }

    @DeleteMapping("/clear")
    public RespBean clearAssets() {
        try {
            assetService.clearAssets();
        } catch (Exception e) {
            return RespBean.error("清空无效资产信息失败!");
        }
        return RespBean.ok(null);
    }

    @PostMapping("/addRelation")
    public RespBean addRelation(@RequestBody OrderAssetData relationData) {
        RespBean response = new RespBean();
        response.setStatus(200);
        response.setMsg("添加资产成功!");
        List<Long> assetIds = relationData.getAssetIds();
        Long oid = relationData.getOid();
        if (CollectionUtils.isEmpty(assetIds) || oid == null) {
            return RespBean.error("参数异常，添加资产失败！");
        }
        try {
            assetService.addAssetOrderRelation(assetIds, oid);
        } catch (Exception e) {
            logger.error("添加资产失败", e);
            return RespBean.error("添加资产失败！");
        }
        return response;
    }

    /**
     * 根据资产id列表，获取资产信息
     *
     * @param initData：初始信息，包括资产id列表，page，size
     * @return RespPageBean
     */
    @PostMapping("/getAssetByAids")
    public RespPageBean getAssetByAids(@RequestBody AssetQueryCondition initData) {
        List<Long> aids = initData.getAids();
        if (CollectionUtils.isEmpty(aids)) {
            return new RespPageBean();
        }
        return assetService.getAssetPageInfoByIds(initData);
    }
}
