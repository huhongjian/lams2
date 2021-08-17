package com.bupt.lams.controller.budget;

import com.bupt.lams.dto.ProjectHeadTableData;
import com.bupt.lams.dto.ProjectQueryCondition;
import com.bupt.lams.model.Plan;
import com.bupt.lams.model.Project;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.model.RespPageBean;
import com.bupt.lams.service.PlanService;
import com.bupt.lams.service.ProjectService;
import com.bupt.lams.utils.UserInfoUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 科研项目相关
 */
@RestController
@RequestMapping("/project")
public class ProjectController {
    private Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Resource
    ProjectService projectService;
    @Resource
    PlanService planService;

    @GetMapping("/get")
    public RespPageBean getProjectInfoByPage(ProjectQueryCondition projectQueryCondition, Date[] dateScope) {
        if (dateScope != null && dateScope.length == 2) {
            projectQueryCondition.setStartDate(dateScope[0]);
            projectQueryCondition.setEndDate(dateScope[1]);
        }
        return projectService.getProjectByCondition(projectQueryCondition);
    }

    @GetMapping("/get/all")
    public RespBean getAllProjectList() {
        RespBean response = new RespBean();
        response.setStatus(200);
        try {
            List<Project> data = projectService.getAllProjects();
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取项目列表失败", e);
            return RespBean.error("获取项目列表失败！");
        }
        return response;
    }

    @PostMapping("/add")
    public RespBean addProject(@RequestBody Project project) {
        RespBean response = new RespBean();
        response.setStatus(200);
        response.setMsg("项目新增成功!");
        try {
            projectService.addProject(project);
        } catch (Exception e) {
            logger.error("新增项目失败", e);
            return RespBean.error("新增项目失败！");
        }
        return response;
    }

    @PutMapping("/edit")
    public RespBean updateProject(@RequestBody Project project) {
        try {
            Project oldProject = projectService.getProjectById(project.getId());
            Double used = oldProject.getFunding() - oldProject.getUnfulfilledQuota();
            if (project.getFunding() < used) {
                return RespBean.error("更新失败!总额度小于已执行额度！");
            }
            projectService.updateProject(project);
        } catch (Exception e) {
            return RespBean.error("更新失败!");
        }
        return RespBean.ok("更新成功!");
    }

    @GetMapping("/headTable")
    public RespBean getHeadTableData() {
        RespBean response = new RespBean();
        response.setStatus(200);
        try {
            ProjectHeadTableData data = projectService.getHeadTableData();
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取科研项目基本统计数据异常！", e);
            return RespBean.error("获取科研项目基本统计数据失败!");
        }
        return response;
    }

    @DeleteMapping("/delete")
    public RespBean deleteProjects(@RequestBody List<Long> pids) {
        // 工单只允许教师删除
        if (UserInfoUtils.isTeacher() == false) {
            return RespBean.error("您没有删除权限，请联系管理员!");
        }
        try {
            List<Plan> plans = planService.getPlansByProjectIds(pids);
            if (CollectionUtils.isNotEmpty(plans)) {
                return RespBean.error("选中的科研项目有预算计划，请先删除预算计划！");
            }
            projectService.deleteProjects(pids);
        } catch (Exception e) {
            return RespBean.error("删除失败!");
        }
        return RespBean.ok("删除成功!");
    }
}
