package com.bupt.lams.service.aop;

import com.bupt.lams.constants.RecordAopDispatchEnum;
import com.bupt.lams.model.Asset;
import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.Record;
import com.bupt.lams.utils.UserInfoUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 操作记录切面
 */
@Aspect
@Component
public class RecordAop {
    private Logger logger = LoggerFactory.getLogger(RecordAop.class);

    @After(value = "(execution(* com.bupt.lams.service.AssetService.updateAsset(..)))")
    public void after(JoinPoint joinPoint) {
        try {
            Record record = new Record();
            LamsUser user = UserInfoUtils.getLoginedUser();
            Asset asset = (Asset) joinPoint.getArgs()[0];
            record.setOperate(RecordAopDispatchEnum.UPDATE_ASSET.getIndex());
            record.setOperator(user);
            String text = "【" + user.getName() + "】" + "修改了资产信息；" + "资产编号：【" + asset.getId() + "】";
            record.setText(text);
        } catch (Exception e) {

        }
    }
}
