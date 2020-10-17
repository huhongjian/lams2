package com.bupt.lams.controller.order;

import com.bupt.lams.constants.AssetStatusEnum;
import com.bupt.lams.dto.OrderQueryCondition;
import com.bupt.lams.model.*;
import com.bupt.lams.service.OrderService;
import com.bupt.lams.utils.POIUtils;
import com.bupt.lams.utils.UserInfoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 工单信息相关
 */
@RestController
@RequestMapping("/order/basic")
public class OrderBasicController {

    private Logger logger = LoggerFactory.getLogger(OrderBasicController.class);

    @Resource
    OrderService orderService;

    @GetMapping("/get")
    public RespPageBean getOrderInByPage(OrderQueryCondition orderQueryCondition) {
        List<Integer> assetStatus = new ArrayList<>();
        assetStatus.add(AssetStatusEnum.CREATE.getIndex());
        assetStatus.add(AssetStatusEnum.NORMAL.getIndex());
        return orderService.getOrderByCondition(orderQueryCondition);
    }

    @PostMapping("/add")
    public RespBean addAsset(@RequestBody Order order) {
        try {
            orderService.addOrderIn(order);
        } catch (Exception e) {
            logger.error("新增资产失败", e);
            return RespBean.error("资产申请采购失败！");
        }
        return RespBean.ok("资产申请采购成功！");
    }

    @PostMapping("/borrow")
    public RespBean borrowAsset(@RequestBody Order order) {
        RespBean response = new RespBean();
        response.setStatus(200);
        try {
            orderService.borrowAsset(order);
        } catch (Exception e) {
            logger.error("资产借出异常！", e);
            response.setStatus(500);
            response.setMsg("资产借出异常，请稍后重试");
        }
        return response;
    }

    @PutMapping("/edit")
    public RespBean updateOrder(@RequestBody Order order) {
        LamsUser user = UserInfoUtils.getLoginedUser();
        // 工单只允许创建人和管理员修改
        if (!order.getUserEmail().equals(user.getUsername()) && isAdmin() == false) {
            return RespBean.error("没有修改权限，请联系管理员!");
        }
        try {
            orderService.updateOrder(order);
        } catch (Exception e) {
            return RespBean.error("更新失败!");
        }
        return RespBean.ok("更新成功!");
    }

    /**
     * 当前用户是否有管理员权限
     *
     * @return
     */
    public boolean isAdmin() {
        LamsUser user = UserInfoUtils.getLoginedUser();
        List<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (role.getName().equals("ROLE_admin")) {
                return true;
            }
        }
        return false;
    }

    @DeleteMapping("/delete")
    public RespBean deleteOrders(@RequestBody List<Integer> oids) {
        // 工单只允许管理员删除
        if (isAdmin() == false) {
            return RespBean.error("没有删除权限，请联系管理员!");
        }
        try {
            orderService.deleteOrders(oids);
        } catch (Exception e) {
            return RespBean.error("删除失败!");
        }
        return RespBean.ok("删除成功!");
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportData(OrderQueryCondition orderQueryCondition) {
        List<Order> list = (List<Order>) orderService.getOrderByCondition(orderQueryCondition).getData();
        return POIUtils.assetIn2Excel(list);
    }
}
