package com.bupt.lams.controller.asset;

import com.bupt.lams.constants.AssetStatusEnum;
import com.bupt.lams.constants.OperateTypeEnum;
import com.bupt.lams.dto.TaskHandleDto;
import com.bupt.lams.dto.WorkflowTaskOperateInfoDto;
import com.bupt.lams.model.Asset;
import com.bupt.lams.model.Hr;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.model.WorkflowOperate;
import com.bupt.lams.service.AssetService;
import com.bupt.lams.service.TaskOperateService;
import com.bupt.lams.utils.UserInfoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 资产状态流转相关
 */
@RestController
@RequestMapping("/asset/task")
public class AssetTaskController {

    @Resource
    TaskOperateService taskOperateService;
    @Resource
    AssetService assetService;

    private Logger logger = LoggerFactory.getLogger(AssetTaskController.class);

    @RequestMapping("handleTask")
    @ResponseBody
    public RespBean handleTask(@RequestBody TaskHandleDto taskHandleDto) {
        RespBean response = new RespBean();
        response.setStatus(200);
        try {
            taskOperateService.claimAndHandleOrder(taskHandleDto, taskHandleDto.getCandidateGroups(), taskHandleDto.getCandidateUsers());
        } catch (
                Exception e) {
            logger.error("工单验证操作失败", e);
            response.setStatus(500);
            response.setMsg("工单验证操异常，请稍后重试");
        }
        return response;
    }

    @RequestMapping("getCandidateTaskBranchInfo")
    @ResponseBody
    public RespBean getCandidateTaskBranchInfo(Long aid) {
        RespBean response = new RespBean();
        WorkflowTaskOperateInfoDto opInfoDto = new WorkflowTaskOperateInfoDto();
        List<WorkflowOperate> operateList = new ArrayList<>();
        try {
            Hr user = UserInfoUtils.getLoginedUser();
            // 1. 获取工单
            Asset asset = assetService.selectByPrimaryKey(aid);
            if (asset == null) {
                response.setStatus(500);
                response.setMsg("指定工单不存在！");
                return response;
            }
            // 如果当前用户是工单创建人并且工单不是关闭状态
            if (asset.getCharger().equals(user.getName()) && !asset.getStatus().equals(AssetStatusEnum.READY.getIndex())) {
                try {
                    opInfoDto = taskOperateService.getWorkflowTaskOperateInfo(user.getName(), aid);
                } catch (RuntimeException e) {
                    logger.info("获取操作信息失败" + e.getMessage(), e);
                } finally {
                    // 撤销操作
                    WorkflowOperate cancel = new WorkflowOperate();
                    cancel.setOperateType(OperateTypeEnum.CANCEL.getIndex());
                    cancel.setOperate(OperateTypeEnum.CANCEL.getName());
                    opInfoDto.getOperateList().add(cancel);
                }
            } else {
                // 2. 根据查询来源设置操作信息
                opInfoDto = taskOperateService.getWorkflowTaskOperateInfo(user.getName(), aid);
            }
        } catch (Exception e) {
            opInfoDto = new WorkflowTaskOperateInfoDto();
        }
        response.setObj(opInfoDto);
        response.setStatus(200);
        return response;
    }
}
