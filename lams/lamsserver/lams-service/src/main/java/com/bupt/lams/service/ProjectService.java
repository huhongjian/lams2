package com.bupt.lams.service;

import com.bupt.lams.constants.AssetStatusEnum;
import com.bupt.lams.dto.ProjectQueryCondition;
import com.bupt.lams.mapper.ProjectMapper;
import com.bupt.lams.model.Asset;
import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.Project;
import com.bupt.lams.model.RespPageBean;
import com.bupt.lams.service.annotation.OperateRecord;
import com.bupt.lams.service.strategies.record.UpdateAssetRecord;
import com.bupt.lams.utils.UserInfoUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目service
 */
@Service
public class ProjectService {
    @Resource
    ProjectMapper projectMapper;

    public RespPageBean getProjectByCondition(ProjectQueryCondition condition) {
        Integer page = condition.getPage();
        Integer size = condition.getSize();
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Project> data = projectMapper.getProjectByCondition(condition);
        return transToRespPageBean(page, size, data);
    }

    @Transactional(rollbackFor = Exception.class)
    public Long addProject(Project project) {
        LamsUser user = UserInfoUtils.getLoginedUser();
        project.setUserEmail(user.getUsername());
        projectMapper.insertSelective(project);
        return project.getId();
    }

    public void updateAsset(Project project) {
        projectMapper.updateProject(project);
    }

    /**
     * 将结果集转化为展示集合
     *
     * @param page
     * @param size
     * @param data
     * @return
     */
    private RespPageBean transToRespPageBean(Integer page, Integer size, List<Project> data) {
        Long total = Long.valueOf(data.size());
        Integer end = Math.min(page + size, data.size());
        // 展示的数据
        List<Project> res = new ArrayList<>();
        for (Integer i = page; i < end; i++) {
            res.add(data.get(i));
        }
        RespPageBean bean = new RespPageBean();
        bean.setData(res);
        bean.setTotal(total);
        return bean;
    }
}
