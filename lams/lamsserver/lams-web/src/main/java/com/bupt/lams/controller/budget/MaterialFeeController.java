package com.bupt.lams.controller.budget;

import com.bupt.lams.model.RespBean;
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

    @GetMapping("/get")
    public RespBean getMaterialFeeList() {
        RespBean response = new RespBean();
        response.setStatus(200);
        try {
            List<MaterialFee> data = materialFeeService.getMaterialFeeList();
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取材料费失败", e);
            return RespBean.error("获取材料费失败！");
        }
        return response;
    }

    @PostMapping("/add")
    public RespBean addMaterialFee(@RequestBody MaterialFee materialFee) {
        RespBean response = new RespBean();
        response.setStatus(200);
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
            return response;
        }
        try {
            List<MaterialFee> data = materialFeeService.getMaterialFeeListByIds(pids);
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取材料费信息失败", e);
            return RespBean.error("获取材料费信息失败！");
        }
        return response;
    }
}
