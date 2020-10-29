package com.bupt.lams.service.strategies.record;

import com.bupt.lams.constants.RecordAopDispatchEnum;
import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.Order;
import com.bupt.lams.model.Record;
import com.bupt.lams.utils.UserInfoUtils;
import org.aspectj.lang.JoinPoint;

import java.util.Date;

/**
 * 借用资产申请操作记录策略类
 */
public class BorrowAssetRecord implements IRecord {

    @Override
    public Record getRecord(JoinPoint joinPoint) {
        Record record = new Record();
        LamsUser user = UserInfoUtils.getLoginedUser();
        Order order = (Order) joinPoint.getArgs()[0];
        record.setOperate(RecordAopDispatchEnum.BORROW_ASSET.getIndex());
        record.setOperator(user);
        String text = "【" + user.getName() + "】" + "借用资产；" + "资产编号：【" + order.getAsset().getId() + "】";
        record.setText(text);
        record.setOperateTime(new Date());
        return record;
    }
}
