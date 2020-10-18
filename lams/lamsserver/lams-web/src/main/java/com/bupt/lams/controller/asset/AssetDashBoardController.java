package com.bupt.lams.controller.asset;

import com.bupt.lams.dto.AssetDashBoardData;
import com.bupt.lams.dto.AssetStatusCount;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.service.AssetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 资产仪表盘相关
 */
@RestController
@RequestMapping("/asset/dashboard/get")
public class AssetDashBoardController {

    private Logger logger = LoggerFactory.getLogger(AssetDashBoardController.class);

    @Resource
    AssetService assetService;

    @GetMapping("/headTable")
    public RespBean getHeadTableData() {
        RespBean response = new RespBean();
        response.setStatus(200);
        try {
            AssetDashBoardData data = assetService.getHeadTableData();
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取资产仪表盘数据异常！", e);
            return RespBean.error("获取仪表盘数据失败!");
        }
        return response;
    }

    @PostMapping("/tableData")
    public RespBean getTableData(@RequestBody List<String> typeList) {
        RespBean response = new RespBean();
        response.setStatus(200);
        try {
            List<AssetStatusCount> data = assetService.getTableData(typeList);
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取资产仪表盘数据异常！", e);
            return RespBean.error("获取仪表盘数据失败!");
        }
        return response;
    }

    @PostMapping("/ringData")
    public RespBean getRingData(@RequestBody List<String> typeList) {
        RespBean response = new RespBean();
        response.setStatus(200);
        try {
            List<AssetStatusCount> data = assetService.getRingData(typeList);
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取资产仪表盘数据异常！", e);
            return RespBean.error("获取仪表盘数据失败!");
        }
        return response;
    }

    @GetMapping("/typeChartData")
    public RespBean getTypeChartData() {
        RespBean response = new RespBean();
        response.setStatus(200);
        try {
            List<AssetStatusCount> data = assetService.getTypeChartData();
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取资产仪表盘数据异常！", e);
            return RespBean.error("获取仪表盘数据失败!");
        }
        return response;
    }

    @GetMapping("/lineData")
    public RespBean getLineData(Date[] monthScope) {
        RespBean response = new RespBean();
        response.setStatus(200);
        try {
            List<AssetDashBoardData> data = assetService.getLineData(monthScope);
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取资产仪表盘数据异常！", e);
            return RespBean.error("获取仪表盘数据失败!");
        }
        return response;
    }
}
