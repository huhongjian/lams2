package com.bupt.lams.controller.order;

import com.bupt.lams.constants.AssetStatusEnum;
import com.bupt.lams.dto.OrderQueryCondition;
import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.Order;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.model.RespPageBean;
import com.bupt.lams.service.OrderService;
import com.bupt.lams.utils.UserInfoUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 学生离退相关
 */
@RestController
@RequestMapping("/stuOut")
public class StudentOutController {
    private Logger logger = LoggerFactory.getLogger(StudentOutController.class);

    @Resource
    OrderService orderService;

    @GetMapping("/get")
    public RespPageBean getStuOutByPage(OrderQueryCondition orderQueryCondition, Date[] dateScope) {
        if (dateScope != null && dateScope.length == 2) {
            orderQueryCondition.setStartDate(dateScope[0]);
            orderQueryCondition.setEndDate(dateScope[1]);
        }
        return orderService.getStuOutByCondition(orderQueryCondition);
    }

    @PostMapping("/add")
    public RespBean addStuOut(@RequestBody Order order) {
        try {
            LamsUser user = UserInfoUtils.getLoginedUser();
            OrderQueryCondition condition = new OrderQueryCondition();
            condition.setUserEmail(user.getUsername());
            List<Integer> assetStatus = new ArrayList<>();
            assetStatus.add(AssetStatusEnum.INUSE.getIndex());
            condition.setAssetStatuses(assetStatus);
            condition.setPage(null);
            condition.setSize(null);
            List<Order> orders = orderService.queryOrderByCondition(condition);
            if (CollectionUtils.isNotEmpty(orders)) {
                return RespBean.error("申请离退失败！请先归还所有资产");
            }
            orderService.addStuOut(order);
        } catch (Exception e) {
            logger.error("新增离退流程失败", e);
            return RespBean.error("申请离退失败！");
        }
        return RespBean.ok("申请离退成功！");
    }
}
