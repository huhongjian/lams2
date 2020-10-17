package com.bupt.lams.controller.asset;

import com.bupt.lams.constants.AssetStatusEnum;
import com.bupt.lams.constants.OrderStatusEnum;
import com.bupt.lams.controller.order.OrderBasicController;
import com.bupt.lams.dto.AssetQueryCondition;
import com.bupt.lams.model.Asset;
import com.bupt.lams.model.Order;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.model.RespPageBean;
import com.bupt.lams.service.AssetService;
import com.bupt.lams.service.OrderAssetService;
import com.bupt.lams.utils.UserInfoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 工单信息相关
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
    public RespPageBean getAssetInfoByPage(AssetQueryCondition assetQueryCondition) {
        return assetService.getAssetByCondition(assetQueryCondition);
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
}
