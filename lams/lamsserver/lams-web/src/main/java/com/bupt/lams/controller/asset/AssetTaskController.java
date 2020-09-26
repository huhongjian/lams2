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
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
            taskOperateService.claimAndHandleTask(taskHandleDto);
        } catch (Exception e) {
            logger.error("当前流程操作失败！", e);
            response.setStatus(500);
            response.setMsg("资产操作异常，请稍后重试");
        }
        return response;
    }

    @RequestMapping("getCandidateTaskBranchInfo")
    @ResponseBody
    public RespBean getCandidateTaskBranchInfo(@RequestParam Long id) {
        RespBean response = new RespBean();
        WorkflowTaskOperateInfoDto opInfoDto;
        Hr user = UserInfoUtils.getLoginedUser();
        // 1. 获取资产信息
        Asset asset = assetService.selectByPrimaryKey(id);
        if (asset == null) {
            response.setStatus(500);
            response.setMsg("指定工单不存在！");
            return response;
        }
        try {
            opInfoDto = taskOperateService.getCandidateOrAssignedOrderWorkflowTaskOperateInfo(user.getName(), id);
        } catch (Exception e) {
            opInfoDto = new WorkflowTaskOperateInfoDto();
        }
        // 如果当前用户是工单创建人并且工单不是终止状态
        if (asset.getCharger().equals(user.getName()) && !asset.getStatus().equals(AssetStatusEnum.READY.getName())) {
            WorkflowOperate cancel = new WorkflowOperate();
            cancel.setOperateType(OperateTypeEnum.CANCEL.getIndex());
            cancel.setOperate(OperateTypeEnum.CANCEL.getName());
            opInfoDto.getOperateList().add(cancel);
        }
        response.setObj(opInfoDto);
        response.setStatus(200);
        return response;
    }
}
