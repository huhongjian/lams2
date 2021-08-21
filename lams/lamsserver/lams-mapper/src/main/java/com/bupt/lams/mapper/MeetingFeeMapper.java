package com.bupt.lams.mapper;

import com.bupt.lams.model.planItem.MeetingFee;

import java.util.List;

public interface MeetingFeeMapper {
    int insertSelective(MeetingFee meetingFee);

    List<MeetingFee> getMeetingFeeByIds(List<Long> ids);

    /**
     * 根据ids获取设备费完整信息，用于执行预算明细
     */
    List<MeetingFee> getFullInfoByIds(List<Long> ids);

    MeetingFee getById(Long id);

    void update(MeetingFee meetingFee);

    void clear(String userEmail);

    List<MeetingFee> getAll();
}
