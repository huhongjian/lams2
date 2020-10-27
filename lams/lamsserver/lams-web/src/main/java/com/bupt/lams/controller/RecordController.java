package com.bupt.lams.controller;

import com.bupt.lams.dto.RecordQueryCondition;
import com.bupt.lams.model.RespPageBean;
import com.bupt.lams.service.RecordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 操作纪律相关
 */
@RestController
@RequestMapping("/record")
public class RecordController {
    @Resource
    RecordService recordService;

    @GetMapping("/get")
    public RespPageBean getRecordByPage(RecordQueryCondition recordQueryCondition, Date[] dateScope) {
        if (dateScope != null && dateScope.length == 2) {
            recordQueryCondition.setStartDate(dateScope[0]);
            recordQueryCondition.setEndDate(dateScope[1]);
        }
        return recordService.getRecordByCondition(recordQueryCondition);
    }
}
