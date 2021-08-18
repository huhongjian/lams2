package com.bupt.lams.mapper;

import com.bupt.lams.model.planItem.MeetingFee;

import java.util.List;

public interface MeetingFeeMapper {

    List<MeetingFee> getMeetingFeeList();

    int insertSelective(MeetingFee meetingFee);

    List<MeetingFee> getMeetingFeeByIds(List<Long> ids);

    MeetingFee getById(Long id);

    void update(MeetingFee meetingFee);
}
