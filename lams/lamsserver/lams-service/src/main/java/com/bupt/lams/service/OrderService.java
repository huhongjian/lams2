package com.bupt.lams.service;

import com.bupt.lams.constants.AssetStatusEnum;
import com.bupt.lams.constants.OrderStatusEnum;
import com.bupt.lams.constants.ProcessTypeEnum;
import com.bupt.lams.dto.OrderQueryCondition;
import com.bupt.lams.mapper.AssetMapper;
import com.bupt.lams.mapper.OrderAssetMapper;
import com.bupt.lams.mapper.OrderMapper;
import com.bupt.lams.model.*;
import com.bupt.lams.service.annotation.OperateRecord;
import com.bupt.lams.service.aop.AddAssetRecord;
import com.bupt.lams.service.aop.BorrowAssetRecord;
import com.bupt.lams.service.aop.DeleteOrderRecord;
import com.bupt.lams.service.aop.UpdateOrderRecord;
import com.bupt.lams.utils.UserInfoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 工单service
 */
@Service
public class OrderService {
    private Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Resource
    AssetMapper assetMapper;
    @Resource
    OrderMapper orderMapper;
    @Resource
    OrderAssetMapper orderAssetMapper;
    @Resource
    TaskOperateService taskOperateService;

    public RespPageBean getOrderByCondition(OrderQueryCondition condition) {
        Integer page = condition.getPage();
        Integer size = condition.getSize();
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        condition.setPage(page);
        List<Order> data = orderMapper.getOrderByCondition(condition);
        Long total = orderMapper.getTotalByCondition(condition);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }

    @Transactional(rollbackFor = Exception.class)
    @OperateRecord(description = "新增资产申请", clazz = AddAssetRecord.class)
    public void addOrderIn(Order order) {
        LamsUser user = UserInfoUtils.getLoginedUser();
        order.setUserEmail(user.getUsername());
        order.setCategory(ProcessTypeEnum.IN.getIndex());
        order.setStatus(OrderStatusEnum.CREATE.getIndex());
        order.setCreateTime(new Date());
        orderMapper.insertSelective(order);
        Asset asset = order.getAsset();
        asset.setStatus(AssetStatusEnum.CREATE.getIndex());
        assetMapper.insertSelective(asset);
        OrderAsset orderAsset = new OrderAsset();
        orderAsset.setAid(asset.getId());
        orderAsset.setOid(order.getId());
        orderAsset.setCreateTime(new Date());
        orderAsset.setUpdateTime(new Date());
        orderAssetMapper.insertSelective(orderAsset);
        try {
            taskOperateService.startWorkFlow(order.getId(), ProcessTypeEnum.IN.getIndex(), user.getUsername(), null);
        } catch (Exception e) {
            logger.error("启动工作流失败", e);
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @OperateRecord(description = "借用资产", clazz = BorrowAssetRecord.class)
    public void borrowAsset(Order order) {
        Asset asset = order.getAsset();
        LamsUser user = UserInfoUtils.getLoginedUser();
        order.setStatus(OrderStatusEnum.ASK.getIndex());
        order.setCreateTime(new Date());
        orderMapper.updateOrder(order);
        assetMapper.updateAsset(asset);
        try {
            // 这个map是开启工作流时间的数据，并不是流转过程中的数据
            taskOperateService.startWorkFlow(order.getId(), ProcessTypeEnum.OUT.getIndex(), user.getUsername(), null);
        } catch (Exception e) {
            logger.error("启动工作流失败", e);
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @OperateRecord(description = "更新工单信息", clazz = UpdateOrderRecord.class)
    public void updateOrder(Order order) {
        orderMapper.updateOrder(order);
        assetMapper.updateAsset(order.getAsset());
    }

    @Transactional(rollbackFor = Exception.class)
    @OperateRecord(description = "删除工单", clazz = DeleteOrderRecord.class)
    public void deleteOrders(List<Integer> oids) {
        orderMapper.deleteManyByOids(oids);
        orderAssetMapper.deleteManyByOids(oids);
    }

    public Order selectByPrimaryKey(Long id) {
        return orderMapper.selectByPrimaryKey(id);
    }
}
