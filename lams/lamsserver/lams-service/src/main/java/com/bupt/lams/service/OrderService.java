package com.bupt.lams.service;

import com.bupt.lams.constants.OperateTypeEnum;
import com.bupt.lams.constants.OrderStatusEnum;
import com.bupt.lams.constants.ProcessTypeEnum;
import com.bupt.lams.dto.OrderQueryCondition;
import com.bupt.lams.mapper.AssetMapper;
import com.bupt.lams.mapper.OrderAssetMapper;
import com.bupt.lams.mapper.OrderMapper;
import com.bupt.lams.model.*;
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

    public RespPageBean getOrderByPage(Integer page, Integer size, Order order, Date[] beginDateScope) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Order> data = orderMapper.getOrderByPage(page, size, order, beginDateScope);
        Long total = orderMapper.getTotal(order, beginDateScope);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }

    public RespPageBean getOrderByCondition(OrderQueryCondition condition) {
        Integer page = condition.getPage();
        Integer size = condition.getSize();
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        condition.setPage(page);
        List<Order> data = orderMapper.getOrderByCondition(condition);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(new Long(condition.getIds().size()));
        return bean;
    }

    @Transactional(rollbackFor = Exception.class)
    public void addOrderIn(Order order) {
        LamsUser user = UserInfoUtils.getLoginedUser();
        order.setCategory(ProcessTypeEnum.IN.getIndex());
        order.setStatus(OrderStatusEnum.CREATE.getName());
        order.setCreateTime(new Date());
        orderMapper.insertSelective(order);
        assetMapper.insertSelective(order.getAsset());
        OrderAsset orderAsset = new OrderAsset();
        orderAsset.setAid(order.getAsset().getId());
        orderAsset.setOid(order.getId());
        orderAsset.setCreateTime(new Date());
        orderAsset.setUpdateTime(new Date());
        orderAssetMapper.insertSelective(orderAsset);
        // 构造record
        Record record = new Record();
        record.setOid(order.getId());
        record.setType(ProcessTypeEnum.IN.getIndex());
        record.setOperate(OperateTypeEnum.CREATE.getName());
        record.setOperateType(OperateTypeEnum.CREATE.getIndex());
        record.setOperator(user.getName());
        record.setOperatorMail(user.getUsername());
        record.setOperateTime(order.getCreateTime());
        try {
            taskOperateService.startWorkFlow(record, null);
        } catch (Exception e) {
            logger.error("启动工作流失败", e);
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void borrowAsset(Order order) {
        Asset asset = order.getAsset();
        LamsUser user = UserInfoUtils.getLoginedUser();
        order.setStatus(OrderStatusEnum.ASK.getName());
        order.setCreateTime(new Date());
        orderMapper.updateOrder(order);
        assetMapper.updateAsset(asset);
        // 构造record
        Record record = new Record();
        record.setOid(order.getId());
        record.setType(ProcessTypeEnum.OUT.getIndex());
        record.setOperate(OperateTypeEnum.BORROW.getName());
        record.setOperateType(OperateTypeEnum.BORROW.getIndex());
        record.setOperator(user.getName());
        record.setOperatorMail(user.getUsername());
        record.setOperateTime(order.getCreateTime());
        try {
            // 这个map是开启工作流时间的数据，并不是流转过程中的数据
            taskOperateService.startWorkFlow(record, null);
        } catch (Exception e) {
            logger.error("启动工作流失败", e);
            throw e;
        }
    }

    public Order selectByPrimaryKey(Long id) {
        return orderMapper.selectByPrimaryKey(id);
    }
}
