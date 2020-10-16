package com.bupt.lams.controller.asset;

import com.bupt.lams.dto.OrderQueryCondition;
import com.bupt.lams.model.*;
import com.bupt.lams.service.OrderService;
import com.bupt.lams.utils.UserInfoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
//
//    @GetMapping("/nations")
//    public List<Nation> getAllNations() {
//        return nationService.getAllNations();
//    }
//
//    @GetMapping("/politicsstatus")
//    public List<Politicsstatus> getAllPoliticsstatus() {
//        return politicsstatusService.getAllPoliticsstatus();
//    }
//
//    @GetMapping("/joblevels")
//    public List<JobLevel> getAllJobLevels() {
//        return jobLevelService.getAllJobLevels();
//    }
//
//    @GetMapping("/positions")
//    public List<Position> getAllPositions() {
//        return positionService.getAllPositions();
//    }
//
//    @GetMapping("/maxWorkID")
//    public RespBean maxWorkID() {
//        return RespBean.build().setStatus(200).setObj(String.format("%08d", employeeService.maxWorkID() + 1));
//    }
//
//    @GetMapping("/deps")
//    public List<Department> getAllDepartments() {
//        return departmentService.getAllDepartments();
//    }
//
//    @GetMapping("/export")
//    public ResponseEntity<byte[]> exportData() {
//        List<Employee> list = (List<Employee>) employeeService.getEmployeeByPage(null, null, new Employee(), null).getData();
//        return POIUtils.employee2Excel(list);
//    }
//
//    @PostMapping("/import")
//    public RespBean importData(MultipartFile file) throws IOException {
//        List<Employee> list = POIUtils.excel2Employee(file, nationService.getAllNations(), politicsstatusService.getAllPoliticsstatus(), departmentService.getAllDepartmentsWithOutChildren(), positionService.getAllPositions(), jobLevelService.getAllJobLevels());
//        if (employeeService.addEmps(list) == list.size()) {
//            return RespBean.ok("上传成功");
//        }
//        return RespBean.error("上传失败");
//    }
}
