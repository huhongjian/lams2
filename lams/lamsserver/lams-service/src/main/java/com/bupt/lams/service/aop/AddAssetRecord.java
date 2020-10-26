package com.bupt.lams.service.aop;

import com.bupt.lams.constants.RecordAopDispatchEnum;
import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.Order;
import com.bupt.lams.model.Record;
import com.bupt.lams.utils.UserInfoUtils;
import org.aspectj.lang.JoinPoint;

import java.util.Date;

/**
 * 新增资产申请操作记录策略类
 */
public class AddAssetRecord implements IRecord {

    @Override
    public Record getRecord(JoinPoint joinPoint) {
        Record record = new Record();
        LamsUser user = UserInfoUtils.getLoginedUser();
        Order order = (Order) joinPoint.getArgs()[0];
        record.setOperate(RecordAopDispatchEnum.ADD_ASSET.getIndex());
        record.setOperator(user);
        String text = "【" + user.getName() + "】" + "新增了资产采购申请；" + "采购单号：【" + order.getId() + "】";
        record.setText(text);
        record.setOperateTime(new Date());
        return record;
    }
}
