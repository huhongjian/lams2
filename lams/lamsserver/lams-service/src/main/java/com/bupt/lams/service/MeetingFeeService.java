package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.mapper.MeetingFeeMapper;
import com.bupt.lams.model.PlanAndItem;
import com.bupt.lams.model.planItem.EquipmentFee;
import com.bupt.lams.model.planItem.MaterialFee;
import com.bupt.lams.model.planItem.MeetingFee;
import com.bupt.lams.utils.ComputeUtils;
import com.bupt.lams.utils.TransferUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
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
        Double total = (meetingFee.getMeal() + meetingFee.getOtherCost()) * meetingFee.getDays() * meetingFee.getAmount() * meetingFee.getMeetingNum() / 10000D;
        meetingFee.setSubTotal(total);
        meetingFee.setUnfulfilledQuota(meetingFee.getSubTotal());
        meetingFee.setExecuteRate(ComputeUtils.computeExecuteRate(meetingFee.getSubTotal(), meetingFee.getUnfulfilledQuota()));
        meetingFeeMapper.insertSelective(meetingFee);
        return meetingFee.getId();
    }

    public PlanItemData getDataByPlanAndItem(List<PlanAndItem> planAndItemList) {
        PlanItemData data = new PlanItemData();
        List<Long> idList = new ArrayList<>();
        if (planAndItemList == null || planAndItemList.size() == 0) {
            return data;
        }
        for (PlanAndItem item : planAndItemList) {
            if (item.getItid() == PlanItemTypeEnum.MEETING.getIndex()) {
                idList.add(item.getIid());
            }
        }
        if (idList.size() == 0) {
            return data;
        }
        data = getMeetingFeeDataByIds(idList);
        return data;
    }

    public PlanItemData getMeetingFeeDataByIds(List<Long> ids) {
        List<MeetingFee> equipmentFeeList = meetingFeeMapper.getMeetingFeeByIds(ids);
        return TransferUtils.transToPlanItemData(new ArrayList<>(equipmentFeeList));
    }

    public List<MeetingFee> getMeetingFeeListByIds(List<Long> pids) {
        return meetingFeeMapper.getMeetingFeeByIds(pids);
    }
}
