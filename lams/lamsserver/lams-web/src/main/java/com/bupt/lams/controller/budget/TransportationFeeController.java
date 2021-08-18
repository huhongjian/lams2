package com.bupt.lams.controller.budget;

import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.model.planItem.PlanItem;
import com.bupt.lams.service.TransportationFeeService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 北京市内交通费相关
 */
@RestController
@RequestMapping("/planItem/transportationFee")
public class TransportationFeeController {
    private Logger logger = LoggerFactory.getLogger(TransportationFeeController.class);

    @Resource
    TransportationFeeService transportationFeeService;

    @GetMapping("/get")
    public RespBean getTransportationFeeList() {
        RespBean response = new RespBean();
        response.setStatus(200);
        try {
            List<PlanItem> data = transportationFeeService.getTransportationFeeList();
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取差旅/会议/国际合作与交流费失败", e);
            return RespBean.error("获取差旅/会议/国际合作与交流费失败！");
        }
        return response;
    }

    @PostMapping("/add")
    public RespBean addTransportationFee(@RequestBody PlanItem transportationFee) {
        RespBean response = new RespBean();
        response.setStatus(200);
        if (transportationFee == null) {
            logger.error("北京市内交通费不能为空！");
            return RespBean.error("北京市内交通费不能为空！");
        }
        if (transportationFee.getSubTotal() == null) {
            logger.error("北京市内交通费合计不能为空！");
            return RespBean.error("北京市内交通费合计不能为空！");
        }
        try {
            Long id = transportationFeeService.addTransportationFee(transportationFee);
            response.setObj(id);
        } catch (Exception e) {
            logger.error("新增差旅/会议/国际合作与交流费失败", e);
            return RespBean.error("新增差旅/会议/国际合作与交流费失败！");
        }
        return response;
    }

    /**
     * 根据预算明细id列表，获取预算明细信息
     */
    @PostMapping("/getByIds")
    public RespBean getTransportationFeeListByIds(@RequestBody List<Long> pids) {
        RespBean response = new RespBean();
        response.setStatus(200);
        if (CollectionUtils.isEmpty(pids)) {
            return response;
        }
        try {
            PlanItemData data = transportationFeeService.getTransportationFeeDataByIds(pids);
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取北京市内交通费信息失败", e);
            return RespBean.error("获取北京市内交通费信息失败！");
        }
        return response;
    }
}
