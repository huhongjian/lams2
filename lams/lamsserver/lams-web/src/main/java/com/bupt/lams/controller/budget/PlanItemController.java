package com.bupt.lams.controller.budget;

import com.bupt.lams.model.planItem.EquipmentFee;
import com.bupt.lams.model.Project;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.model.planItem.MaterialFee;
import com.bupt.lams.service.EquipmentFeeService;
import com.bupt.lams.service.MaterialFeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 预算明细相关
 */
@RestController
@RequestMapping("/planItem")
public class PlanItemController {
    private Logger logger = LoggerFactory.getLogger(PlanItemController.class);

    @Resource
    EquipmentFeeService equipmentService;

    @Resource
    MaterialFeeService materialFeeService;

    @GetMapping("/get/equipmentFee")
    public RespBean getEquipmentFeeList() {
        RespBean response = new RespBean();
        response.setStatus(200);
        response.setMsg("获取设备费成功!");
        try {
            List<EquipmentFee> data = equipmentService.getEquipmentFeeList();
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取设备费失败", e);
            return RespBean.error("获取设备费失败！");
        }
        return response;
    }

    @GetMapping("/get/materialFee")
    public RespBean getMaterialFeeList() {
        RespBean response = new RespBean();
        response.setStatus(200);
        response.setMsg("获取材料费成功!");
        try {
            List<MaterialFee> data = materialFeeService.getMaterialFeeList();
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取材料费失败", e);
            return RespBean.error("获取材料费失败！");
        }
        return response;
    }

    @PostMapping("/add/equipmentFee")
    public RespBean addEquipmentFee(@RequestBody EquipmentFee equipmentFee) {
        RespBean response = new RespBean();
        response.setStatus(200);
        response.setMsg("设备费新增成功!");
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

    @PostMapping("/add/materialFee")
    public RespBean addMaterialFee(@RequestBody MaterialFee materialFee) {
        RespBean response = new RespBean();
        response.setStatus(200);
        response.setMsg("材料费新增成功!");
        if (materialFee == null) {
            logger.error("材料费不能为空！");
            return RespBean.error("材料费不能为空！");
        }
        if (materialFee.getUnitPrice() == null && materialFee.getSubTotal() == null) {
            logger.error("材料单价和合计不能同时为空！");
            return RespBean.error("材料单价和合计不能同时为空！");
        }
        if (materialFee.getAmount() == null) {
            materialFee.setAmount(0);
        }
        try {
            materialFeeService.addMaterialFee(materialFee);
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
