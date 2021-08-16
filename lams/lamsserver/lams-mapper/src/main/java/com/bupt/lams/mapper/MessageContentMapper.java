package com.bupt.lams.mapper;

import com.bupt.lams.model.MessageContent;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface MessageContentMapper {
    Integer updateMailSendLogStatus(@Param("msgId") String msgId, @Param("status") Integer status);

    Integer insert(MessageContent messageContent);

    MessageContent getContentById(Long id);

    Integer updateCount(@Param("msgId") String msgId, @Param("date") Date date);
}
