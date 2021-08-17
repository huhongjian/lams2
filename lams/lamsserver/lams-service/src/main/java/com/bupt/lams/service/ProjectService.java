package com.bupt.lams.service;

import com.bupt.lams.dto.ProjectHeadTableData;
import com.bupt.lams.dto.ProjectQueryCondition;
import com.bupt.lams.mapper.PlanMapper;
import com.bupt.lams.mapper.ProjectMapper;
import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.Project;
import com.bupt.lams.model.RespPageBean;
import com.bupt.lams.utils.ComputeUtils;
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
    @Resource
    PlanMapper planMapper;

    public RespPageBean getProjectByCondition(ProjectQueryCondition condition) {
        Integer page = condition.getPage();
        Integer size = condition.getSize();
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Project> data = projectMapper.getProjectByCondition(condition);
        return transToRespPageBean(page, size, data);
    }

    public List<Project> getAllProjects() {
        return projectMapper.getAllProjects();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long addProject(Project project) {
        LamsUser user = UserInfoUtils.getLoginedUser();
        project.setUserEmail(user.getUsername());
        project.setUnfulfilledQuota(project.getFunding());
        project.setExecuteRate(0.0);
        projectMapper.insertSelective(project);
        return project.getId();
    }

    public void updateProject(Project project) {
        LamsUser user = UserInfoUtils.getLoginedUser();
        project.setUserEmail(user.getUsername());
        Project oldProject = projectMapper.getProjectById(project.getId());
        // 如果总额有更新, 则更新相关数据
        if (oldProject.getFunding() != project.getFunding()) {
            Double change = oldProject.getFunding() - project.getFunding();
            double quota = project.getUnfulfilledQuota() + change;
            project.setUnfulfilledQuota(quota);
            project.setExecuteRate(ComputeUtils.computeExecuteRate(project.getFunding(), quota));
        }
        projectMapper.updateProject(project);
    }

    public Project getProjectById(Long id) {
        return projectMapper.getProjectById(id);
    }

    public ProjectHeadTableData getHeadTableData() {
        ProjectHeadTableData data = new ProjectHeadTableData();
        Double total = projectMapper.getTotal();
        Double unfulfilledQuota = projectMapper.getUnfulfilledQuota();
        data.setTotal(total);
        data.setLeft(unfulfilledQuota);
        data.setUsed(total - unfulfilledQuota);
        data.setInPlan(planMapper.getTotal());
        data.setExecuteRate(ComputeUtils.computeExecuteRate(total, unfulfilledQuota));
        return data;
    }

    public void deleteProjects(List<Long> pids) {
        projectMapper.deleteProjects(pids);
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
