package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.mapper.BudgetRecordMapper;
import com.bupt.lams.mapper.MeetingFeeMapper;
import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.PlanAndItem;
import com.bupt.lams.model.planItem.MeetingFee;
import com.bupt.lams.utils.ComputeUtils;
import com.bupt.lams.utils.TransferUtils;
import com.bupt.lams.utils.UserInfoUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 会议费service
 */
@Service
public class MeetingFeeService {
    @Resource
    MeetingFeeMapper meetingFeeMapper;
    @Resource
    BudgetRecordMapper budgetRecordMapper;

    @Transactional(rollbackFor = Exception.class)
    public Long addMeetingFee(MeetingFee meetingFee) {
        meetingFee.setItemType(PlanItemTypeEnum.MEETING.getIndex());
        meetingFee.setUserEmail(UserInfoUtils.getLoginedUser().getUsername());
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
        List<MeetingFee> meetingFeeList = meetingFeeMapper.getFullInfoByIds(idList);
        data = TransferUtils.transToPlanItemData(new ArrayList<>(meetingFeeList));
        return data;
    }

    public PlanItemData getMeetingFeeDataByIds(List<Long> ids) {
        List<MeetingFee> equipmentFeeList = meetingFeeMapper.getMeetingFeeByIds(ids);
        return TransferUtils.transToPlanItemData(new ArrayList<>(equipmentFeeList));
    }

    public List<MeetingFee> getMeetingFeeListByIds(List<Long> pids) {
        return meetingFeeMapper.getMeetingFeeByIds(pids);
    }

    public void update(MeetingFee meetingFee) {
        meetingFeeMapper.update(meetingFee);
    }

    public MeetingFee getById(Long id) {
        return meetingFeeMapper.getById(id);
    }

    /**
     * 清空本人创建的、多余的预算明细信息，即，没有和预算计划关联的预算明细信息
     */
    public void clear() {
        LamsUser user = UserInfoUtils.getLoginedUser();
        meetingFeeMapper.clear(user.getUsername());
    }

    public List<MeetingFee> getRecommendFee() {
        // 查询所有设备费信息
        List<MeetingFee> meetingFeeList = meetingFeeMapper.getAll();
        if (CollectionUtils.isEmpty(meetingFeeList)) {
            return new ArrayList<>();
        }
        // 获取最近一条设备费执行记录
        Long id = budgetRecordMapper.getRecentIdByItemType(PlanItemTypeEnum.MEETING.getIndex());
        if (id == null) {
            return meetingFeeList;
        }
        MeetingFee fee = meetingFeeMapper.getById(id);
        if (fee == null) {
            return meetingFeeList;
        }
        // 计算相似度，并按照相似度逆序排列
        for (MeetingFee e : meetingFeeList) {
            e.computeSimilar(fee);
        }
        Collections.sort(meetingFeeList, Comparator.comparingDouble(x -> -x.getSimilar()));
        return meetingFeeList;
    }
}
