package com.bupt.lams.controller.budget;

import com.bupt.lams.model.ProjectType;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.service.ProjectTypesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 项目类型
 */
@RestController
@RequestMapping("/project/types")
public class ProjectTypeController {
    private Logger logger = LoggerFactory.getLogger(ProjectTypeController.class);
    @Resource
    ProjectTypesService projectTypesService;

    @GetMapping("/get")
    public RespBean getProjectTypes() {
        RespBean response = new RespBean();
        response.setStatus(200);
        try {
            List<ProjectType> types = projectTypesService.getAllProjectTypes();
            response.setObj(types);
        } catch (Exception e) {
            logger.error("获取科研项目类型失败", e);
            return RespBean.error("获取科研项目类型失败！");
        }
        return response;
    }
}
