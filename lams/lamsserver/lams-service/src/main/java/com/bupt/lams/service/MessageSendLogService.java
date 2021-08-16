package com.bupt.lams.service;

import com.bupt.lams.mapper.MessageSendLogMapper;
import com.bupt.lams.model.MessageSendLog;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class MessageSendLogService {
    @Resource
    MessageSendLogMapper messageSendLogMapper;

    public Integer updateMailSendLogStatus(String msgId, Integer status) {
        return messageSendLogMapper.updateMailSendLogStatus(msgId, status);
    }

    public Integer insert(MessageSendLog mailSendLog) {
        return messageSendLogMapper.insert(mailSendLog);
    }

    public List<MessageSendLog> getMailSendLogsByStatus() {
        return messageSendLogMapper.getMailSendLogsByStatus();
    }

    public Integer updateCount(String msgId, Date date) {
        return messageSendLogMapper.updateCount(msgId, date);
    }
}
