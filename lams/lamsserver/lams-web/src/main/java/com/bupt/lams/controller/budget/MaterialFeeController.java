package com.bupt.lams.controller.budget;

import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.model.planItem.EquipmentFee;
import com.bupt.lams.model.planItem.MaterialFee;
import com.bupt.lams.service.MaterialFeeService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 材料费相关
 */
@RestController
@RequestMapping("/planItem/materialFee")
public class MaterialFeeController {
    private Logger logger = LoggerFactory.getLogger(MaterialFeeController.class);

    @Resource
    MaterialFeeService materialFeeService;

    @PostMapping("/add")
    public RespBean addMaterialFee(@RequestBody MaterialFee materialFee) {
        RespBean response = new RespBean();
        response.setStatus(200);
        if (materialFee == null) {
            logger.error("材料费不能为空！");
            return RespBean.error("材料费不能为空！");
        }
        if (materialFee.getUnitPrice() == null && materialFee.getSubTotal() == null) {
            logger.error("材料费单价和合计不能同时为空！");
            return RespBean.error("材料费单价和合计不能同时为空！");
        }
        if (materialFee.getAmount() == null) {
            materialFee.setAmount(0);
        }
        try {
            Long id = materialFeeService.addMaterialFee(materialFee);
            response.setObj(id);
        } catch (Exception e) {
            logger.error("新增材料费失败", e);
            return RespBean.error("新增材料费失败！");
        }
        return response;
    }

    /**
     * 根据预算明细id列表，获取预算明细信息
     */
    @PostMapping("/getByIds")
    public RespBean getMaterialFeeByIds(@RequestBody List<Long> pids) {
        RespBean response = new RespBean();
        response.setStatus(200);
        if (CollectionUtils.isEmpty(pids)) {
            response.setObj(new PlanItemData());
            return response;
        }
        try {
            PlanItemData data = materialFeeService.getMaterialFeeDataByIds(pids);
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取材料费信息失败", e);
            return RespBean.error("获取材料费信息失败！");
        }
        return response;
    }

    @DeleteMapping("/clear")
    public RespBean clear() {
        try {
            materialFeeService.clear();
        } catch (Exception e) {
            return RespBean.error("清空无效预算明细失败!");
        }
        return RespBean.ok(null);
    }

    /**
     * 获取推荐的预算明细
     */
    @GetMapping("/getRecommend")
    public RespBean getRecommend() {
        RespBean response = new RespBean();
        response.setStatus(200);
        try {
            List<MaterialFee> data = materialFeeService.getRecommendFee();
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取推荐材料费信息失败", e);
            return RespBean.error("获取推荐材料费信息失败！");
        }
        return response;
    }

    @PutMapping("/edit")
    public RespBean editMaterialFee(@RequestBody MaterialFee materialFee) {
        RespBean response = new RespBean();
        response.setStatus(200);
        if (materialFee == null) {
            logger.error("材料费不能为空！");
            return RespBean.error("材料费不能为空！");
        }
        if (materialFee.getUnitPrice() == null && materialFee.getSubTotal() == null) {
            logger.error("材料费单价和合计不能同时为空！");
            return RespBean.error("材料费单价和合计不能同时为空！");
        }
        if (materialFee.getAmount() == null) {
            materialFee.setAmount(0);
        }
        try {
            materialFeeService.update(materialFee);
        } catch (Exception e) {
            logger.error("编辑材料费失败", e);
            return RespBean.error("编辑材料费失败！" + e.getMessage());
        }
        return response;
    }
}
