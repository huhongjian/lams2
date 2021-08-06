package com.bupt.lams.controller.budget;

import com.bupt.lams.model.PlanItemType;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.service.PlanItemTypesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 预算明细类型
 */
@RestController
@RequestMapping("/plan/item/types")
public class PlanItemTypeController {
    private Logger logger = LoggerFactory.getLogger(PlanItemTypeController.class);
    @Resource
    PlanItemTypesService planItemTypesService;

    @GetMapping("/get")
    public RespBean getPlanItemTypes() {
        RespBean response = new RespBean();
        response.setStatus(200);
        try {
            List<PlanItemType> types = planItemTypesService.getAllPlanItemTypes();
            response.setObj(types);
        } catch (Exception e) {
            logger.error("获取预算明细类型失败", e);
            return RespBean.error("获取预算明细类型失败！");
        }
        return response;
    }
}
