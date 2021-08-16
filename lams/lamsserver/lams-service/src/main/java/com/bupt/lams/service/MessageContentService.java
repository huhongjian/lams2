package com.bupt.lams.service;

import com.bupt.lams.mapper.MessageContentMapper;
import com.bupt.lams.model.MessageContent;
import com.bupt.lams.model.MessageSendLog;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class MessageContentService {
    @Resource
    MessageContentMapper messageContentMapper;

    public Integer updateMailSendLogStatus(String msgId, Integer status) {
        return messageContentMapper.updateMailSendLogStatus(msgId, status);
    }

    public Integer insert(MessageContent messageContent) {
        return messageContentMapper.insert(messageContent);
    }

    public MessageContent getContentById(Long id) {
        return messageContentMapper.getContentById(id);
    }

    public Integer updateCount(String msgId, Date date) {
        return messageContentMapper.updateCount(msgId, date);
    }
}
