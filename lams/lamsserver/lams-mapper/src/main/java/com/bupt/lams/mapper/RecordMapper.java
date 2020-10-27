package com.bupt.lams.mapper;

import com.bupt.lams.dto.RecordQueryCondition;
import com.bupt.lams.model.Record;

import java.util.List;

/**
 * 操作记录mapper
 */
public interface RecordMapper {
    List<Record> getRecordByCondition(RecordQueryCondition condition);

    Long getTotalByCondition(RecordQueryCondition condition);

    Record selectByPrimaryKey(Long id);

    void deleteByPrimaryKey(Long id);

    void insertSelective(Record record);
}
