package com.bupt.lams.controller.budget;

import com.bupt.lams.model.Project;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.model.planItem.EquipmentFee;
import com.bupt.lams.service.EquipmentFeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 预算明细相关
 */
@RestController
@RequestMapping("/planItem/equipmentFee")
public class EquipmentFeeController {
    private Logger logger = LoggerFactory.getLogger(EquipmentFeeController.class);

    @Resource
    EquipmentFeeService equipmentService;

    @GetMapping("/get")
    public RespBean getEquipmentFeeList() {
        RespBean response = new RespBean();
        response.setStatus(200);
        try {
            List<EquipmentFee> data = equipmentService.getEquipmentFeeList();
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取设备费失败", e);
            return RespBean.error("获取设备费失败！");
        }
        return response;
    }

    @PostMapping("/add")
    public RespBean addEquipmentFee(@RequestBody EquipmentFee equipmentFee) {
        RespBean response = new RespBean();
        response.setStatus(200);
        if (equipmentFee == null) {
            logger.error("设备费不能为空！");
            return RespBean.error("设备费不能为空！");
        }
        if (equipmentFee.getUnitPrice() == null) {
            logger.error("设备单价不能为空！");
            return RespBean.error("设备单价不能为空！");
        }
        if (equipmentFee.getAmount() == null) {
            equipmentFee.setAmount(0);
        }
        try {
            equipmentService.addEquipmentFee(equipmentFee);
        } catch (Exception e) {
            logger.error("新增设备费失败", e);
            return RespBean.error("新增设备费失败！");
        }
        return response;
    }

    @PutMapping("/edit")
    public RespBean updateProject(@RequestBody Project project) {
        try {
            equipmentService.updateAsset(project);
        } catch (Exception e) {
            return RespBean.error("更新失败!");
        }
        return RespBean.ok("更新成功!");
    }
}
