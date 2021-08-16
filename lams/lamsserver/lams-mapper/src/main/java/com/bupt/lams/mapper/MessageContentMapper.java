package com.bupt.lams.mapper;

import com.bupt.lams.dto.WarningRecordQueryCondition;
import com.bupt.lams.model.MessageContent;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface MessageContentMapper {
    List<MessageContent> getByCondition(WarningRecordQueryCondition condition);

    Integer updateMailSendLogStatus(@Param("msgId") String msgId, @Param("status") Integer status);

    Integer insert(MessageContent messageContent);

    MessageContent getContentById(Long id);

    Integer updateCount(@Param("msgId") String msgId, @Param("date") Date date);
}
