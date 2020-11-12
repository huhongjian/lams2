package com.bupt.lams.service.strategies.taskhandle;

import com.bupt.lams.constants.AssetStatusEnum;
import com.bupt.lams.constants.OrderStatusEnum;
import com.bupt.lams.dto.TaskHandleDto;
import com.bupt.lams.model.Asset;
import com.bupt.lams.model.Order;
import com.bupt.lams.service.AssetService;
import com.bupt.lams.service.OrderService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 归还
 */
@Component
public class HandleReturn implements IUpdateStatus {
    @Resource
    AssetService assetService;
    @Resource
    OrderService orderService;

    @Override
    public void updateStage(TaskHandleDto taskHandleDto, Order order) {
        order.setStatus(OrderStatusEnum.READY.getIndex());
        orderService.updateOrderStatusById(order);
        // 清空过期时间和申请理由信息
        orderService.resetOrderById(order.getId());
        // 参数中的order不包含资产信息，获取全部资产信息
        order = orderService.selectFullOrderInfoById(order.getId());
        // 更新资产状态
        Asset asset = order.getAsset();
        asset.setStatus(AssetStatusEnum.FREE.getIndex());
        assetService.changeAssetStatus(asset);
    }
}
