package com.bupt.lams.controller.budget;

import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.model.planItem.AcademicFee;
import com.bupt.lams.model.planItem.MeetingFee;
import com.bupt.lams.service.MeetingFeeService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 会议费相关
 */
@RestController
@RequestMapping("/planItem/meetingFee")
public class MeetingFeeController {
    private Logger logger = LoggerFactory.getLogger(MeetingFeeController.class);

    @Resource
    MeetingFeeService meetingFeeService;

    @GetMapping("/get")
    public RespBean getMeetingFeeList() {
        RespBean response = new RespBean();
        response.setStatus(200);
        try {
            List<MeetingFee> data = meetingFeeService.getMeetingFeeList();
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取会议费失败", e);
            return RespBean.error("获取会议费失败！");
        }
        return response;
    }

    @PostMapping("/add")
    public RespBean addMeetingFee(@RequestBody MeetingFee meetingFee) {
        RespBean response = new RespBean();
        response.setStatus(200);
        if (meetingFee == null) {
            logger.error("会议费不能为空！");
            return RespBean.error("会议费不能为空！");
        }
        try {
            Long id = meetingFeeService.addMeetingFee(meetingFee);
            response.setObj(id);
        } catch (Exception e) {
            logger.error("新增会议费失败", e);
            return RespBean.error("新增会议费失败！");
        }
        return response;
    }

    /**
     * 根据预算明细id列表，获取预算明细信息
     */
    @PostMapping("/getByIds")
    public RespBean getMeetingFeeByIds(@RequestBody List<Long> pids) {
        RespBean response = new RespBean();
        response.setStatus(200);
        if (CollectionUtils.isEmpty(pids)) {
            return response;
        }
        try {
            PlanItemData data = meetingFeeService.getMeetingFeeDataByIds(pids);
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取会议费信息失败", e);
            return RespBean.error("获取会议费信息失败！");
        }
        return response;
    }
}
