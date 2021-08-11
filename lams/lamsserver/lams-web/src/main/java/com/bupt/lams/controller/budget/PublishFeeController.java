package com.bupt.lams.controller.budget;

import com.bupt.lams.model.RespBean;
import com.bupt.lams.model.planItem.AcademicFee;
import com.bupt.lams.model.planItem.PublishFee;
import com.bupt.lams.service.PublishFeeService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 差旅/会议/国际合作与交流费相关
 */
@RestController
@RequestMapping("/planItem/publishFee")
public class PublishFeeController {
    private Logger logger = LoggerFactory.getLogger(PublishFeeController.class);

    @Resource
    PublishFeeService publishFeeService;

    @GetMapping("/get")
    public RespBean getPublishFeeList() {
        RespBean response = new RespBean();
        response.setStatus(200);
        try {
            List<PublishFee> data = publishFeeService.getPublishFeeList();
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取出版/文献/信息传播/知识产权事务费失败", e);
            return RespBean.error("获取出版/文献/信息传播/知识产权事务费失败！");
        }
        return response;
    }

    @PostMapping("/add")
    public RespBean addPublishFee(@RequestBody PublishFee publishFee) {
        RespBean response = new RespBean();
        response.setStatus(200);
        if (publishFee == null) {
            logger.error("出版/文献/信息传播/知识产权事务费不能为空！");
            return RespBean.error("出版/文献/信息传播/知识产权事务费不能为空！");
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
            return response;
        }
        try {
            List<PublishFee> data = publishFeeService.getPublishFeeListByIds(pids);
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取差旅/会议/国际合作与交流费信息失败", e);
            return RespBean.error("获取差旅/会议/国际合作与交流费信息失败！");
        }
        return response;
    }
}
