package com.bupt.lams.service.aop;

import com.bupt.lams.constants.RecordAopDispatchEnum;
import com.bupt.lams.model.Asset;
import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.Record;
import com.bupt.lams.utils.UserInfoUtils;
import org.aspectj.lang.JoinPoint;

import java.util.Date;

/**
 * 修改资产状态操作记录策略类
 */
public class ChangeAssetStatusRecord implements IRecord {

    @Override
    public Record getRecord(JoinPoint joinPoint) {
        Record record = new Record();
        LamsUser user = UserInfoUtils.getLoginedUser();
        Asset asset = (Asset) joinPoint.getArgs()[0];
        record.setOperate(RecordAopDispatchEnum.CHANGE_ASSET_STATUS.getIndex());
        record.setOperator(user);
        String text = "【" + user.getName() + "】" + "修改资产状态为：" +
                "【" + asset.getStatusName() + "】；" + "资产编号：【" + asset.getId() + "】";
        record.setText(text);
        record.setOperateTime(new Date());
        return record;
    }
}
