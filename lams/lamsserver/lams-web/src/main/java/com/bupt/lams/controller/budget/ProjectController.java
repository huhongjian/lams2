package com.bupt.lams.controller.budget;

import com.bupt.lams.dto.ProjectQueryCondition;
import com.bupt.lams.model.Project;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.model.RespPageBean;
import com.bupt.lams.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 科研项目相关
 */
@RestController
@RequestMapping("/project")
public class ProjectController {
    private Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Resource
    ProjectService projectService;

    @GetMapping("/get")
    public RespPageBean getProjectInfoByPage(ProjectQueryCondition projectQueryCondition, Date[] dateScope) {
        if (dateScope != null && dateScope.length == 2) {
            projectQueryCondition.setStartDate(dateScope[0]);
            projectQueryCondition.setEndDate(dateScope[1]);
        }
        return projectService.getProjectByCondition(projectQueryCondition);
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
            projectService.updateAsset(project);
        } catch (Exception e) {
            return RespBean.error("更新失败!");
        }
        return RespBean.ok("更新成功!");
    }
}
