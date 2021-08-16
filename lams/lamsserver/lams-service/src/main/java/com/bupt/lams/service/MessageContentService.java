package com.bupt.lams.service;

import com.bupt.lams.dto.WarningRecordQueryCondition;
import com.bupt.lams.mapper.MessageContentMapper;
import com.bupt.lams.model.MessageContent;
import com.bupt.lams.model.RespPageBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MessageContentService {
    @Resource
    MessageContentMapper messageContentMapper;

    public RespPageBean getMessageContentByCondition(WarningRecordQueryCondition condition) {
        Integer page = condition.getPage();
        Integer size = condition.getSize();
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<MessageContent> data = messageContentMapper.getByCondition(condition);
        return transToRespPageBean(page, size, data);
    }

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

    private RespPageBean transToRespPageBean(Integer page, Integer size, List<MessageContent> data) {
        Long total = Long.valueOf(data.size());
        Integer end = Math.min(page + size, data.size());
        // 展示的数据
        List<MessageContent> res = new ArrayList<>();
        for (Integer i = page; i < end; i++) {
            res.add(data.get(i));
        }
        RespPageBean bean = new RespPageBean();
        bean.setData(res);
        bean.setTotal(total);
        return bean;
    }
}
