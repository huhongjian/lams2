package com.bupt.lams.mapper;

import com.bupt.lams.model.Record;

import java.util.List;

/**
 * 操作记录mapper
 */
public interface RecordMapper {
    List<Record> getAllRecord();

    Record selectByPrimaryKey(Long id);

    void deleteByPrimaryKey(Long id);

    void insertSelective(Record record);
}
