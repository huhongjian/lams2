package com.bupt.lams.service.strategies.taskhandle;

import com.bupt.lams.constants.AssetStatusEnum;
import com.bupt.lams.constants.OrderStatusEnum;
import com.bupt.lams.dto.TaskHandleDto;
import com.bupt.lams.mapper.AssetMapper;
import com.bupt.lams.mapper.OrderAssetMapper;
import com.bupt.lams.model.Asset;
import com.bupt.lams.model.Order;
import com.bupt.lams.service.OrderService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 入库操作
 */
@Component
public class HandleAssetIn implements IUpdateStatus {
    @Resource
    OrderAssetMapper orderAssetMapper;
    @Resource
    OrderService orderService;
    @Resource
    AssetMapper assetMapper;

    @Override
    public void updateStage(TaskHandleDto taskHandleDto, Order order) {
        // 更新入库工单状态
        order.setStatus(OrderStatusEnum.READY.getIndex());
        orderService.updateOrderStatusById(order);
        // 更新资产入库时间
        Asset asset = new Asset();
        List<Long> aids = orderAssetMapper.getAidListByOid(taskHandleDto.getId());
        for (Long aid : aids) {
            asset.setId(aid);
            asset.setStatus(AssetStatusEnum.FREE.getIndex());
            asset.setReadyDate(new Date());
            assetMapper.updateAsset(asset);
        }
    }
}
