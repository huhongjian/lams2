package com.bupt.lams.service.aop;

import com.bupt.lams.constants.RecordAopDispatchEnum;
import com.bupt.lams.dto.TaskHandleDto;
import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.Record;
import com.bupt.lams.utils.UserInfoUtils;
import org.aspectj.lang.JoinPoint;

import java.util.Date;

/**
 * 取消工单操作记录策略类
 */
public class CancelRecord implements IRecord {

    @Override
    public Record getRecord(JoinPoint joinPoint) {
        Record record = new Record();
        LamsUser user = UserInfoUtils.getLoginedUser();
        TaskHandleDto task = (TaskHandleDto) joinPoint.getArgs()[0];
        record.setOperate(RecordAopDispatchEnum.CANCEL.getIndex());
        record.setOperator(user);
        String text = "【" + user.getName() + "】" + "取消了工单；" + "工单号：【" + task.getId() + "】";
        record.setText(text);
        record.setOperateTime(new Date());
        return record;
    }
}
