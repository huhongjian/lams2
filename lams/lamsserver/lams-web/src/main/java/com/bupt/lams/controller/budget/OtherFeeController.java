package com.bupt.lams.controller.budget;

import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.model.planItem.EquipmentFee;
import com.bupt.lams.model.planItem.OtherFee;
import com.bupt.lams.service.OtherFeeService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 其他支出相关
 */
@RestController
@RequestMapping("/planItem/otherFee")
public class OtherFeeController {
    private Logger logger = LoggerFactory.getLogger(OtherFeeController.class);

    @Resource
    OtherFeeService otherFeeService;

    @PostMapping("/add")
    public RespBean addOtherFee(@RequestBody OtherFee otherFee) {
        RespBean response = new RespBean();
        response.setStatus(200);
        if (otherFee == null) {
            logger.error("其他支出不能为空！");
            return RespBean.error("其他支出不能为空！");
        }
        if (otherFee.getSubTotal() == null) {
            logger.error("合计不能为空！");
            return RespBean.error("合计不能为空！");
        }
        try {
            Long id = otherFeeService.addOtherFee(otherFee);
            response.setObj(id);
        } catch (Exception e) {
            logger.error("新增其他支出失败", e);
            return RespBean.error("新增其他支出失败！");
        }
        return response;
    }

    /**
     * 根据预算明细id列表，获取预算明细信息
     */
    @PostMapping("/getByIds")
    public RespBean getOtherFeeByIds(@RequestBody List<Long> pids) {
        RespBean response = new RespBean();
        response.setStatus(200);
        if (CollectionUtils.isEmpty(pids)) {
            response.setObj(new PlanItemData());
            return response;
        }
        try {
            PlanItemData data = otherFeeService.getOtherFeeDataByIds(pids);
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取其他支出信息失败", e);
            return RespBean.error("获取其他支出信息失败！");
        }
        return response;
    }

    @DeleteMapping("/clear")
    public RespBean clear() {
        try {
            otherFeeService.clear();
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
            List<OtherFee> data = otherFeeService.getRecommendFee();
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取推荐其他支出信息失败", e);
            return RespBean.error("获取推荐其他支出信息失败！");
        }
        return response;
    }

    @PutMapping("/edit")
    public RespBean editOtherFee(@RequestBody OtherFee otherFee) {
        RespBean response = new RespBean();
        response.setStatus(200);
        if (otherFee == null) {
            logger.error("其他支出不能为空！");
            return RespBean.error("其他支出不能为空！");
        }
        if (otherFee.getSubTotal() == null) {
            logger.error("合计不能为空！");
            return RespBean.error("合计不能为空！");
        }
        try {
            otherFeeService.update(otherFee);
        } catch (Exception e) {
            logger.error("编辑其他支出失败", e);
            return RespBean.error("编辑其他支出失败！" + e.getMessage());
        }
        return response;
    }
}
