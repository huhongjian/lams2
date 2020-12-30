package com.bupt.lams.controller.order;

import com.bupt.lams.dto.AssetQueryCondition;
import com.bupt.lams.dto.OrderQueryCondition;
import com.bupt.lams.model.Order;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.model.RespPageBean;
import com.bupt.lams.service.AssetService;
import com.bupt.lams.service.OrderService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 学生离退相关
 */
@RestController
@RequestMapping("/stuOut")
public class StudentOutController {
    private Logger logger = LoggerFactory.getLogger(StudentOutController.class);

    @Resource
    OrderService orderService;
    @Resource
    AssetService assetService;

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
            RespPageBean assetReturn = assetService.getReturn(new AssetQueryCondition());
            if (CollectionUtils.isNotEmpty(assetReturn.getData())) {
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
