package com.bupt.lams.service.aop;

import com.bupt.lams.constants.OperateTypeEnum;
import com.bupt.lams.constants.RecordAopDispatchEnum;
import com.bupt.lams.dto.TaskHandleDto;
import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.Record;
import com.bupt.lams.utils.UserInfoUtils;
import org.aspectj.lang.JoinPoint;

import java.util.Date;

/**
 * 处理工单操作记录策略类
 */
public class HandleRecord implements IRecord {

    @Override
    public Record getRecord(JoinPoint joinPoint) {
        Record record = new Record();
        LamsUser user = UserInfoUtils.getLoginedUser();
        TaskHandleDto task = (TaskHandleDto) joinPoint.getArgs()[0];
        record.setOperate(RecordAopDispatchEnum.HANDLE.getIndex());
        record.setOperator(user);
        String text = "【" + user.getName() + "】" + OperateTypeEnum.getNameByIndex(task.getOperateType()) +
                "了工单；" + "工单号：【" + task.getId() + "】";
        record.setText(text);
        record.setOperateTime(new Date());
        return record;
    }
}
