package com.bupt.lams.service.strategies.record;

import com.bupt.lams.constants.RecordAopDispatchEnum;
import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.Record;
import com.bupt.lams.utils.UserInfoUtils;
import org.aspectj.lang.JoinPoint;

import java.util.Date;
import java.util.List;

/**
 * 删除工单操作记录策略类
 */
public class DeleteOrderRecord implements IRecord {

    @Override
    public Record getRecord(JoinPoint joinPoint) {
        Record record = new Record();
        LamsUser user = UserInfoUtils.getLoginedUser();
        List<Integer> oids = (List<Integer>) joinPoint.getArgs()[0];
        record.setOperate(RecordAopDispatchEnum.DELETE_ORDERS.getIndex());
        record.setOperator(user);
        StringBuffer sb = new StringBuffer();
        for (Integer oid : oids) {
            sb.append(oid);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        String text = "【" + user.getName() + "】" + "删除工单；" + "删除的工单号：【" + sb.toString() + "】";
        record.setText(text);
        record.setOperateTime(new Date());
        return record;
    }
}
