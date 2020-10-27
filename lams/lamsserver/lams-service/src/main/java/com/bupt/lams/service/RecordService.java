package com.bupt.lams.service;

import com.bupt.lams.dto.RecordQueryCondition;
import com.bupt.lams.mapper.RecordMapper;
import com.bupt.lams.model.Record;
import com.bupt.lams.model.RespPageBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RecordService {
    @Resource
    RecordMapper recordMapper;

    public RespPageBean getRecordByCondition(RecordQueryCondition condition) {
        Integer page = condition.getPage();
        Integer size = condition.getSize();
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        condition.setPage(page);
        List<Record> data = recordMapper.getRecordByCondition(condition);
        Long total = recordMapper.getTotalByCondition(condition);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }
}
