package com.bupt.lams.service.aop;

import com.bupt.lams.mapper.RecordMapper;
import com.bupt.lams.model.Record;
import com.bupt.lams.service.annotation.OperateRecord;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * 操作记录切面
 */
@Aspect
@Component
public class RecordAop {
    private Logger logger = LoggerFactory.getLogger(RecordAop.class);

    @Resource
    RecordMapper recordMapper;

    @After(value = "@annotation(op)")
    public void after(JoinPoint joinPoint, OperateRecord op) {
        try {
            Class clz = op.clazz();
            Constructor constructor = clz.getConstructor();
            Method getRecord = clz.getMethod("getRecord", JoinPoint.class);
            Record record = (Record) getRecord.invoke(constructor.newInstance(), joinPoint);
            recordMapper.insertSelective(record);
        } catch (Exception e) {
            logger.error("处理操作记录错误！", e);
        }
    }
}
