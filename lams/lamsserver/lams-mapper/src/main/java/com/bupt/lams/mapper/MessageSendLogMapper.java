package com.bupt.lams.mapper;

import com.bupt.lams.model.MessageSendLog;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface MessageSendLogMapper {
    Integer updateMailSendLogStatus(@Param("msgId") String msgId, @Param("status") Integer status);

    Integer insert(MessageSendLog mailSendLog);

    List<MessageSendLog> getMailSendLogsByStatus();

    Integer updateCount(@Param("msgId") String msgId, @Param("date") Date date);
}
