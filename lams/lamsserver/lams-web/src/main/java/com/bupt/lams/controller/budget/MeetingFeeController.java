package com.bupt.lams.controller.budget;

import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.model.planItem.EquipmentFee;
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

    @PostMapping("/add")
    public RespBean addMeetingFee(@RequestBody MeetingFee meetingFee) {
        RespBean response = new RespBean();
        response.setStatus(200);
        if (meetingFee == null) {
            logger.error("会议费不能为空！");
            return RespBean.error("会议费不能为空！");
        }
        if (meetingFee.getSubTotal() == null && (meetingFee.getMeal() == null || meetingFee.getOtherCost() == null || meetingFee.getDays() == null || meetingFee.getAmount() == null || meetingFee.getMeetingNum() == null)) {
            logger.error("计价方式不能同时为空！");
            return RespBean.error("计价方式不能同时为空！");
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
            response.setObj(new PlanItemData());
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

    @DeleteMapping("/clear")
    public RespBean clear() {
        try {
            meetingFeeService.clear();
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
            List<MeetingFee> data = meetingFeeService.getRecommendFee();
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取推荐会议费信息失败", e);
            return RespBean.error("获取推荐会议费信息失败！");
        }
        return response;
    }

    @PutMapping("/edit")
    public RespBean editMeetingFee(@RequestBody MeetingFee meetingFee) {
        RespBean response = new RespBean();
        response.setStatus(200);
        if (meetingFee == null) {
            logger.error("会议费不能为空！");
            return RespBean.error("会议费不能为空！");
        }
        if (meetingFee.getSubTotal() == null && (meetingFee.getMeal() == null || meetingFee.getOtherCost() == null || meetingFee.getDays() == null || meetingFee.getAmount() == null || meetingFee.getMeetingNum() == null)) {
            logger.error("计价方式不能同时为空！");
            return RespBean.error("计价方式不能同时为空！");
        }
        try {
            meetingFeeService.update(meetingFee);
        } catch (Exception e) {
            logger.error("编辑会议费失败", e);
            return RespBean.error("编辑会议费失败！" + e.getMessage());
        }
        return response;
    }
}
