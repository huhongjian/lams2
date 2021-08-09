package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.mapper.MeetingFeeMapper;
import com.bupt.lams.model.planItem.MeetingFee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 会议费service
 */
@Service
public class MeetingFeeService {
    @Resource
    MeetingFeeMapper meetingFeeMapper;

    public List<MeetingFee> getMeetingFeeList() {
        return meetingFeeMapper.getMeetingFeeList();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long addMeetingFee(MeetingFee meetingFee) {
        meetingFee.setItemType(PlanItemTypeEnum.MEETING.getIndex());
        // 计算合计
        if (meetingFee.getSubTotal() == null) {
            meetingFee.setSubTotal(0.0);
        }
        meetingFeeMapper.insertSelective(meetingFee);
        return meetingFee.getId();
    }
}
