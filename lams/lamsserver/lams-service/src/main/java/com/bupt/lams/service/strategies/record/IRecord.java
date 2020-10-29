package com.bupt.lams.service.strategies.record;

import com.bupt.lams.model.Record;
import org.aspectj.lang.JoinPoint;

public interface IRecord {
    Record getRecord(JoinPoint joinPoint);
}
