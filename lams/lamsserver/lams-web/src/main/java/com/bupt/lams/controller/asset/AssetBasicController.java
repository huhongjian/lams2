package com.bupt.lams.controller.asset;

import com.bupt.lams.constants.WorkflowConstant;
import com.bupt.lams.model.*;
import com.bupt.lams.service.*;
import com.bupt.lams.utils.POIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资产信息相关
 */
@RestController
@RequestMapping("/asset/basic")
public class AssetBasicController {

    private Logger logger = LoggerFactory.getLogger(AssetBasicController.class);


    @Autowired
    EmployeeService employeeService;
    @Autowired
    AssetService assetService;
    @Autowired
    NationService nationService;
    @Autowired
    PoliticsstatusService politicsstatusService;
    @Autowired
    JobLevelService jobLevelService;
    @Autowired
    PositionService positionService;
    @Autowired
    DepartmentService departmentService;
    @Resource
    TaskOperateService taskOperateService;

    @GetMapping("/out")
    public RespPageBean getAssetByPage(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, Asset asset, Date[] beginDateScope) {
        return assetService.getAssetByPage(page, size, asset, beginDateScope);
    }

    @GetMapping("/in")
    public RespPageBean getAssetInByPage(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, AssetIn assetIn, Date[] beginDateScope) {
        return assetService.getAssetInByPage(page, size, assetIn, beginDateScope);
    }

    @PostMapping("/")
    public RespBean addAsset(@RequestBody AssetIn assetIn) {
        RespBean respBean = new RespBean();
        assetService.addAssetIn(assetIn);
        try {
            // todo 构造record
            Record record = new Record();
            taskOperateService.startWorkFlow(record, getDefaultStartWorkFlowParamsMap());
            respBean.setStatus(200);
        } catch (Exception e) {
            logger.error("工单启动工作流失败", e);
            e.printStackTrace();
        }
        return respBean;
    }

    /**
     * 初始化流程启动参数
     *
     * @return 启动参数Map
     */
    private Map<String, String> getDefaultStartWorkFlowParamsMap() {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(WorkflowConstant.WORKFLOW_PARAM_KEY_FIRST_ASSIGNEE, null);
        paramsMap.put(WorkflowConstant.WORKFLOW_PARAM_KEY_RESPONSE_TO_ASSIGNEE, null);
        return paramsMap;
    }

    @DeleteMapping("/{id}")
    public RespBean deleteEmpByEid(@PathVariable Integer id) {
        if (employeeService.deleteEmpByEid(id) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    @PutMapping("/")
    public RespBean updateEmp(@RequestBody Employee employee) {
        if (employeeService.updateEmp(employee) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @GetMapping("/nations")
    public List<Nation> getAllNations() {
        return nationService.getAllNations();
    }

    @GetMapping("/politicsstatus")
    public List<Politicsstatus> getAllPoliticsstatus() {
        return politicsstatusService.getAllPoliticsstatus();
    }

    @GetMapping("/joblevels")
    public List<JobLevel> getAllJobLevels() {
        return jobLevelService.getAllJobLevels();
    }

    @GetMapping("/positions")
    public List<Position> getAllPositions() {
        return positionService.getAllPositions();
    }

    @GetMapping("/maxWorkID")
    public RespBean maxWorkID() {
        RespBean respBean = RespBean.build().setStatus(200)
                .setObj(String.format("%08d", employeeService.maxWorkID() + 1));
        return respBean;
    }

    @GetMapping("/deps")
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportData() {
        List<Employee> list = (List<Employee>) employeeService.getEmployeeByPage(null, null, new Employee(), null).getData();
        return POIUtils.employee2Excel(list);
    }

    @PostMapping("/import")
    public RespBean importData(MultipartFile file) throws IOException {
        List<Employee> list = POIUtils.excel2Employee(file, nationService.getAllNations(), politicsstatusService.getAllPoliticsstatus(), departmentService.getAllDepartmentsWithOutChildren(), positionService.getAllPositions(), jobLevelService.getAllJobLevels());
        if (employeeService.addEmps(list) == list.size()) {
            return RespBean.ok("上传成功");
        }
        return RespBean.error("上传失败");
    }
}
