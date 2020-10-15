package com.bupt.lams.controller.asset;

import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.Order;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.model.RespPageBean;
import com.bupt.lams.service.OrderService;
import com.bupt.lams.utils.UserInfoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 工单信息相关
 */
@RestController
@RequestMapping("/order/basic")
public class OrderBasicController {

    private Logger logger = LoggerFactory.getLogger(OrderBasicController.class);

    @Autowired
    OrderService orderService;

    @GetMapping("/get")
    public RespPageBean getOrderInByPage(@RequestParam Integer category, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, Order order, Date[] beginDateScope) {
        LamsUser user = UserInfoUtils.getLoginedUser();
        order.setCategory(category);
        order.setUserEmail(user.getUsername());
        return orderService.getOrderByPage(page, size, order, beginDateScope);
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

//    @DeleteMapping("/{id}")
//    public RespBean deleteEmpByEid(@PathVariable Integer id) {
//        if (employeeService.deleteEmpByEid(id) == 1) {
//            return RespBean.ok("删除成功!");
//        }
//        return RespBean.error("删除失败!");
//    }

//    @PutMapping("/edit")
//    public RespBean updateEmp(@RequestBody Employee employee) {
//        if (employeeService.updateEmp(employee) == 1) {
//            return RespBean.ok("更新成功!");
//        }
//        return RespBean.error("更新失败!");
//    }
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
