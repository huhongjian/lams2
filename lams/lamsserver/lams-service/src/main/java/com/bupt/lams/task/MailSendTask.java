package com.bupt.lams.task;

import com.bupt.lams.constants.BudgetConstant;
import com.bupt.lams.constants.WarningFreqEnum;
import com.bupt.lams.model.*;
import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class MailSendTask {
    @Autowired
    MessageSendLogService messageSendLogService;
    @Resource
    WarningConfigService warningConfigService;
    @Resource
    PlanService planService;
    @Resource
    ProjectService projectService;
    @Resource
    MessageContentService messageContentService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 失败重发，重发3次
     * 任务10s执行一次
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void mailResendTask() {
        List<MessageSendLog> logs = messageSendLogService.getMailSendLogsByStatus();
        if (logs == null || logs.size() == 0) {
            return;
        }
        logs.forEach(messageSendLog -> {
            if (messageSendLog.getCount() >= 3) {
                messageSendLogService.updateMailSendLogStatus(messageSendLog.getMsgId(), 2);//直接设置该条消息发送失败
            } else {
                messageSendLogService.updateCount(messageSendLog.getMsgId(), new Date());
                MessageContent content = messageContentService.getContentById(messageSendLog.getContentId());
                rabbitTemplate.convertAndSend(MessageConstants.MESSAGE_EXCHANGE_NAME, MessageConstants.MESSAGE_ROUTING_KEY_NAME, content, new CorrelationData(messageSendLog.getMsgId()));
            }
        });
    }

    /**
     * 按天报警任务
     * 每天10点报警
     */
    @Scheduled(cron = "0 0 10 ? * *")
    public void dayWarningTask() {
        List<WarningConfig> configs = warningConfigService.getConfigByFreq(WarningFreqEnum.DAY.getIndex());
        sendWarningMessage(configs);
    }

    /**
     * 每周报警任务
     * 每周一10点报警
     */
    @Scheduled(cron = "0 0 10 ? * MON")
    public void weekWarningTask() {
        List<WarningConfig> configs = warningConfigService.getConfigByFreq(WarningFreqEnum.WEEK.getIndex());
        sendWarningMessage(configs);
    }

    @Transactional(rollbackFor = Exception.class)
    public void sendWarningMessage(List<WarningConfig> configs) {
        if (configs == null || configs.size() == 0) {
            return;
        }
        configs.forEach(conf -> {
            List<String> toUser = conf.getToUser();
            if (CollectionUtils.isEmpty(toUser)) {
                return;
            }
            for (String user : toUser) {
                MessageContent content = buildContent(conf, user);
                if (content == null) {
                    continue;
                }
                messageContentService.insert(content);

                String msgId = UUID.randomUUID().toString();
                MessageSendLog messageSendLog = new MessageSendLog();
                messageSendLog.setMsgId(msgId);
                messageSendLog.setCreateTime(new Date());
                messageSendLog.setExchange(MessageConstants.MESSAGE_EXCHANGE_NAME);
                messageSendLog.setRouteKey(MessageConstants.MESSAGE_ROUTING_KEY_NAME);
                messageSendLog.setContentId(content.getId());
                messageSendLog.setTryTime(new Date(System.currentTimeMillis() + 1000 * 60 * MessageConstants.MSG_TIMEOUT));
                messageSendLogService.insert(messageSendLog);
                rabbitTemplate.convertAndSend(MessageConstants.MESSAGE_EXCHANGE_NAME, MessageConstants.MESSAGE_ROUTING_KEY_NAME, content, new CorrelationData(msgId));
            }
        });
    }

    private MessageContent buildContent(WarningConfig config, String toUser) {
        if (config.getItemType() != null) {
            if (config.getProjectId() == null || config.getPlanId() == null) {
                return null;
            }
            // 构造邮件内容
            return buildContentWithItemType(config, toUser);
        }
        if (config.getPlanId() != null) {
            return buildContentWithPlan(config, toUser);
        }
        if (config.getProjectId() != null) {
            return buildContentWithProject(config, toUser);
        }
        return null;
    }

    private MessageContent buildContentWithItemType(WarningConfig config, String toUser) {
        MessageContent content = new MessageContent();
        Plan planInfo = planService.getFullPlanInfoById(config.getPlanId());
        if (planInfo == null) {
            return null;
        }

        content.setToEmail(toUser);
        content.setProjectName(planInfo.getProjectName());
        content.setPlanName(planInfo.getPlanName());
        content.setItemTypeName(config.getItemTypeName());
        content.setNeedExecuteRate(config.getExecuteRate() + "");

        String dataType = BudgetConstant.ItemTypeDataMap.get(config.getItemType());
        PlanItemData planItemData = planInfo.getPlanItemDataMap().get(dataType);
        if (planItemData == null || CollectionUtils.isEmpty(planItemData.getPlanItemList())) {
            return null;
        }
        if (config.getExecuteRate() <= planItemData.getExecuteRate()) {
            return null;
        }
        content.setExecuteRate(planItemData.getExecuteRate() + "");
        return content;
    }

    private MessageContent buildContentWithPlan(WarningConfig config, String toUser) {
        MessageContent content = new MessageContent();
        Plan planInfo = planService.getFullPlanInfoById(config.getPlanId());
        if (planInfo == null) {
            return null;
        }
        if (config.getExecuteRate() <= planInfo.getExecuteRate()) {
            return null;
        }

        content.setToEmail(toUser);
        content.setProjectName(planInfo.getProjectName());
        content.setPlanName(planInfo.getPlanName());
        content.setNeedExecuteRate(config.getExecuteRate() + "");
        content.setExecuteRate(planInfo.getExecuteRate() + "");
        return content;
    }

    private MessageContent buildContentWithProject(WarningConfig config, String toUser) {
        MessageContent content = new MessageContent();
        Project project = projectService.getProjectById(config.getProjectId());
        if (project == null) {
            return null;
        }
        if (config.getExecuteRate() <= project.getExecuteRate()) {
            return null;
        }

        content.setToEmail(toUser);
        content.setProjectName(project.getProjectName());
        content.setNeedExecuteRate(config.getExecuteRate() + "");
        content.setExecuteRate(project.getExecuteRate() + "");
        return content;
    }
}
