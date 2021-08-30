package com.bupt.lams.service;

import com.bupt.lams.constants.ConfigStatusEnum;
import com.bupt.lams.dto.ExecutionQueryCondition;
import com.bupt.lams.dto.WarningConfigQueryCondition;
import com.bupt.lams.mapper.WarningConfigMapper;
import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.Project;
import com.bupt.lams.model.RespPageBean;
import com.bupt.lams.model.WarningConfig;
import com.bupt.lams.utils.UserInfoUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 预算报警配置service
 */
@Service
public class WarningConfigService {
    private Logger logger = LoggerFactory.getLogger(WarningConfigService.class);

    @Resource
    WarningConfigMapper warningConfigMapper;

    public RespPageBean getWarningConfigByCondition(WarningConfigQueryCondition condition) {
        Integer page = condition.getPage();
        Integer size = condition.getSize();
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<WarningConfig> data = warningConfigMapper.getWarningConfigByCondition(condition);
        return transToRespPageBean(page, size, data);
    }

    @Transactional(rollbackFor = Exception.class)
    public Long addWarningConfig(WarningConfig warningConfig) {
        LamsUser user = UserInfoUtils.getLoginedUser();
        warningConfig.setUserEmail(user.getUsername());
        warningConfig.setConfigStatus(ConfigStatusEnum.ON.getIndex());
        warningConfigMapper.insertSelective(warningConfig);
        return warningConfig.getId();
    }

    public List<WarningConfig> getConfigByFreq(Integer freq) {
        WarningConfigQueryCondition condition = new WarningConfigQueryCondition();
        condition.setFreq(freq);
        condition.setCurrentDate(new Date());
        return warningConfigMapper.getWarningConfigByCondition(condition);
    }

    /**
     * 将结果集转化为展示集合
     *
     * @param page
     * @param size
     * @param data
     * @return
     */
    private RespPageBean transToRespPageBean(Integer page, Integer size, List<WarningConfig> data) {
        Long total = Long.valueOf(data.size());
        Integer end = Math.min(page + size, data.size());
        // 展示的数据
        List<WarningConfig> res = new ArrayList<>();
        for (Integer i = page; i < end; i++) {
            res.add(data.get(i));
        }
        RespPageBean bean = new RespPageBean();
        bean.setData(res);
        bean.setTotal(total);
        return bean;
    }

    public void deleteConfigs(List<Long> ids) {
        warningConfigMapper.deleteConfigs(ids);
    }

    public void update(WarningConfig config) {
        config.setStartTime(config.getDateScope()[0]);
        config.setEndTime(config.getDateScope()[1]);
        warningConfigMapper.update(config);
    }

    @Transactional(rollbackFor = Exception.class)
    public void pauseConfig(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        for (Long id : ids) {
            WarningConfig config = new WarningConfig();
            config.setId(id);
            config.setConfigStatus(ConfigStatusEnum.STOP.getIndex());
            warningConfigMapper.update(config);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void startConfig(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        for (Long id : ids) {
            WarningConfig config = new WarningConfig();
            config.setId(id);
            config.setConfigStatus(ConfigStatusEnum.ON.getIndex());
            warningConfigMapper.update(config);
        }
    }
}
