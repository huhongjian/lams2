package com.bupt.lams.service.aop;

import com.bupt.lams.constants.RecordAopDispatchEnum;
import com.bupt.lams.model.Asset;
import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.Record;
import com.bupt.lams.utils.UserInfoUtils;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 修改资产信息操作记录策略类
 */
@Service
public class UpdateAssetRecord implements IRecord {

    @Override
    public Record getRecord(JoinPoint joinPoint) {
        Record record = new Record();
        LamsUser user = UserInfoUtils.getLoginedUser();
        Asset asset = (Asset) joinPoint.getArgs()[0];
        record.setOperate(RecordAopDispatchEnum.UPDATE_ASSET.getIndex());
        record.setOperator(user);
        String text = "【" + user.getName() + "】" + "修改了资产信息；" + "资产编号：【" + asset.getId() + "】";
        record.setText(text);
        record.setOperateTime(new Date());
        return record;
    }
}
