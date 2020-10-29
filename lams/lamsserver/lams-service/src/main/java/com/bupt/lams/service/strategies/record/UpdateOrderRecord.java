package com.bupt.lams.service.strategies.record;

import com.bupt.lams.constants.RecordAopDispatchEnum;
import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.Order;
import com.bupt.lams.model.Record;
import com.bupt.lams.utils.UserInfoUtils;
import org.aspectj.lang.JoinPoint;

import java.util.Date;

/**
 * 编辑工单信息操作记录策略类
 */
public class UpdateOrderRecord implements IRecord {

    @Override
    public Record getRecord(JoinPoint joinPoint) {
        Record record = new Record();
        LamsUser user = UserInfoUtils.getLoginedUser();
        Order order = (Order) joinPoint.getArgs()[0];
        record.setOperate(RecordAopDispatchEnum.UPDATE_ORDER.getIndex());
        record.setOperator(user);
        String text = "【" + user.getName() + "】" + "编辑工单信息；" + "工单编号：【" + order.getId() + "】";
        record.setText(text);
        record.setOperateTime(new Date());
        return record;
    }
}
