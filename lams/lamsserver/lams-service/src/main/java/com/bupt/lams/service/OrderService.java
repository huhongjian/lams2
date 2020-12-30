package com.bupt.lams.service;

import com.bupt.lams.constants.OrderStatusEnum;
import com.bupt.lams.constants.ProcessTypeEnum;
import com.bupt.lams.dto.OrderQueryCondition;
import com.bupt.lams.mapper.OrderMapper;
import com.bupt.lams.model.Asset;
import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.Order;
import com.bupt.lams.model.RespPageBean;
import com.bupt.lams.service.annotation.OperateRecord;
import com.bupt.lams.service.strategies.record.*;
import com.bupt.lams.utils.UserInfoUtils;
import org.apache.commons.collections4.CollectionUtils;
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
    OrderMapper orderMapper;
    @Resource
    OrderAssetService orderAssetService;
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

    public RespPageBean getStuOutByCondition(OrderQueryCondition condition) {
        Integer page = condition.getPage();
        Integer size = condition.getSize();
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        condition.setPage(page);
        List<Order> data = orderMapper.getStuOutByCondition(condition);
        Long total = orderMapper.getStuOutTotalByCondition(condition);
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
        orderAssetService.updateOrderAssetRelation(order);
        try {
            taskOperateService.startWorkFlow(order, ProcessTypeEnum.IN.getIndex(), null);
        } catch (Exception e) {
            logger.error("启动工作流失败", e);
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void addStuOut(Order order) {
        LamsUser user = UserInfoUtils.getLoginedUser();
        order.setUserEmail(user.getUsername());
        order.setCategory(ProcessTypeEnum.LEAVE.getIndex());
        order.setStatus(OrderStatusEnum.STU_OUT.getIndex());
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        orderMapper.insertSelective(order);
        try {
            taskOperateService.startWorkFlow(order, ProcessTypeEnum.LEAVE.getIndex(), null);
        } catch (Exception e) {
            logger.error("启动工作流失败", e);
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @OperateRecord(description = "借用资产", clazz = BorrowAssetRecord.class)
    public void borrowAsset(Order order) {
        LamsUser user = UserInfoUtils.getLoginedUser();
        order.setUserEmail(user.getUsername());
        order.setCategory(ProcessTypeEnum.OUT.getIndex());
        order.setStatus(OrderStatusEnum.ASK.getIndex());
        order.setCreateTime(new Date());
        orderMapper.insertSelective(order);
        orderAssetService.updateOrderAssetRelation(order);
        try {
            // 这个map是开启工作流时间的数据，并不是流转过程中的数据
            taskOperateService.startWorkFlow(order, ProcessTypeEnum.OUT.getIndex(), null);
        } catch (Exception e) {
            logger.error("启动工作流失败", e);
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @OperateRecord(description = "归还资产", clazz = ReturnAssetRecord.class)
    public void returnAsset(Order order) {
        List<Asset> assetList = order.getAssetList();
        LamsUser user = UserInfoUtils.getLoginedUser();
        order.setUserEmail(user.getUsername());
        order.setCategory(ProcessTypeEnum.RETURN.getIndex());
        order.setStatus(OrderStatusEnum.RETURNING.getIndex());
        order.setCreateTime(new Date());
        orderMapper.insertSelective(order);
        orderAssetService.updateOrderAssetRelation(order);
        try {
            // 这个map是开启工作流时间的数据，并不是流转过程中的数据
            taskOperateService.startWorkFlow(order, ProcessTypeEnum.RETURN.getIndex(), null);
        } catch (Exception e) {
            logger.error("启动工作流失败", e);
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @OperateRecord(description = "更新工单信息", clazz = UpdateOrderRecord.class)
    public void updateOrder(Order order) {
        orderMapper.updateOrder(order);
        orderAssetService.updateOrderAssetRelation(order);
    }

    @OperateRecord(description = "删除工单", clazz = DeleteOrderRecord.class)
    public void deleteOrders(List<Long> oids) {
        // 这里只删除工单信息，没有删除和资产的关联关系
        // 为了防止资产信息被getCur查出来，含义发生改变
        orderMapper.deleteManyByOids(oids);
    }

    public Order selectFullOrderInfoById(Long id) {
        OrderQueryCondition condition = new OrderQueryCondition();
        condition.setOid(id);
        condition.setPage(null);
        condition.setSize(null);
        List<Order> orderList = orderMapper.getOrderByCondition(condition);
        if (CollectionUtils.isEmpty(orderList)) {
            return null;
        }
        return orderList.get(0);
    }

    public Order selectBaseOrderInfoById(Long id) {
        return orderMapper.selectBaseOrderInfoById(id);
    }

    public void updateOrderStatusById(Order order) {
        orderMapper.updateOrderStatusById(order);
    }

    public void updateUserEmailById(Order order) {
        orderMapper.updateUserEmailById(order);
    }

    public List<Order> queryOrderByCondition(OrderQueryCondition condition) {
        List<Order> data = orderMapper.getOrderByCondition(condition);
        return data;
    }
}
