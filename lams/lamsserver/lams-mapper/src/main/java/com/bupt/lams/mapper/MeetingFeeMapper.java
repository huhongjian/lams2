package com.bupt.lams.mapper;

import com.bupt.lams.model.planItem.MeetingFee;
import com.bupt.lams.model.planItem.PlanItem;

import java.util.List;

public interface MeetingFeeMapper {

    List<MeetingFee> getMeetingFeeList();

    int insertSelective(MeetingFee meetingFee);
}
