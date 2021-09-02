package com.bupt.lams.controller.budget;

import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.model.planItem.EquipmentFee;
import com.bupt.lams.model.planItem.PublishFee;
import com.bupt.lams.service.PublishFeeService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 出版/文献/信息传播/知识产权事务费相关
 */
@RestController
@RequestMapping("/planItem/publishFee")
public class PublishFeeController {
    private Logger logger = LoggerFactory.getLogger(PublishFeeController.class);

    @Resource
    PublishFeeService publishFeeService;

    @PostMapping("/add")
    public RespBean addPublishFee(@RequestBody PublishFee publishFee) {
        RespBean response = new RespBean();
        response.setStatus(200);
        if (publishFee == null) {
            logger.error("出版/文献/信息传播/知识产权事务费不能为空！");
            return RespBean.error("出版/文献/信息传播/知识产权事务费不能为空！");
        }
        if (publishFee.getSubTotal() == null) {
            logger.error("合计不能为空！");
            return RespBean.error("合计不能为空！");
        }
        try {
            Long id = publishFeeService.addPublishFee(publishFee);
            response.setObj(id);
        } catch (Exception e) {
            logger.error("新增出版/文献/信息传播/知识产权事务费失败", e);
            return RespBean.error("新增出版/文献/信息传播/知识产权事务费失败！");
        }
        return response;
    }

    /**
     * 根据预算明细id列表，获取预算明细信息
     */
    @PostMapping("/getByIds")
    public RespBean getPublishFeeByIds(@RequestBody List<Long> pids) {
        RespBean response = new RespBean();
        response.setStatus(200);
        if (CollectionUtils.isEmpty(pids)) {
            response.setObj(new PlanItemData());
            return response;
        }
        try {
            PlanItemData data = publishFeeService.getPublishFeeDataByIds(pids);
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取出版/文献/信息传播/知识产权事务费信息失败", e);
            return RespBean.error("获取出版/文献/信息传播/知识产权事务费信息失败！");
        }
        return response;
    }

    @DeleteMapping("/clear")
    public RespBean clear() {
        try {
            publishFeeService.clear();
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
            List<PublishFee> data = publishFeeService.getRecommendFee();
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取推荐出版/文献/信息传播/知识产权事务费信息失败", e);
            return RespBean.error("获取推荐出版/文献/信息传播/知识产权事务费信息失败！");
        }
        return response;
    }

    @PutMapping("/edit")
    public RespBean editPublishFee(@RequestBody PublishFee publishFee) {
        RespBean response = new RespBean();
        response.setStatus(200);
        if (publishFee == null) {
            logger.error("出版/文献/信息传播/知识产权事务费不能为空！");
            return RespBean.error("出版/文献/信息传播/知识产权事务费不能为空！");
        }
        if (publishFee.getSubTotal() == null) {
            logger.error("合计不能为空！");
            return RespBean.error("合计不能为空！");
        }
        try {
            publishFeeService.update(publishFee);
        } catch (Exception e) {
            logger.error("编辑出版/文献/信息传播/知识产权事务费失败", e);
            return RespBean.error("编辑出版/文献/信息传播/知识产权事务费失败！" + e.getMessage());
        }
        return response;
    }
}
