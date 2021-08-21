package com.bupt.lams.controller.budget;

import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.model.planItem.EquipmentFee;
import com.bupt.lams.service.EquipmentFeeService;
import org.apache.commons.collections4.CollectionUtils;
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
            Long id = equipmentService.addEquipmentFee(equipmentFee);
            response.setObj(id);
        } catch (Exception e) {
            logger.error("新增设备费失败", e);
            return RespBean.error("新增设备费失败！");
        }
        return response;
    }

    /**
     * 根据预算明细id列表，获取预算明细信息
     */
    @PostMapping("/getByIds")
    public RespBean getEquipmentFeeByIds(@RequestBody List<Long> pids) {
        RespBean response = new RespBean();
        response.setStatus(200);
        if (CollectionUtils.isEmpty(pids)) {
            response.setObj(new PlanItemData());
            return response;
        }
        try {
            PlanItemData data = equipmentService.getEquipmentFeeDataByIds(pids);
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取设备费信息失败", e);
            return RespBean.error("获取设备费信息失败！");
        }
        return response;
    }

    /**
     * 获取推荐的预算明细
     */
    @GetMapping("/getRecommend")
    public RespBean getRecommend() {
        RespBean response = new RespBean();
        response.setStatus(200);
        try {
            List<EquipmentFee> data = equipmentService.getRecommendFee();
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取推荐设备费信息失败", e);
            return RespBean.error("获取推荐设备费信息失败！");
        }
        return response;
    }

    @DeleteMapping("/clear")
    public RespBean clear() {
        try {
            equipmentService.clear();
        } catch (Exception e) {
            return RespBean.error("清空无效预算明细失败!");
        }
        return RespBean.ok(null);
    }
}
