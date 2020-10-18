package com.bupt.lams.controller.asset;

import com.bupt.lams.dto.AssetDashBoardHeadTableData;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.service.AssetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 资产仪表盘相关
 */
@RestController
@RequestMapping("/asset/dashboard")
public class AssetDashBoardController {

    private Logger logger = LoggerFactory.getLogger(AssetDashBoardController.class);

    @Resource
    AssetService assetService;

    @GetMapping("/get")
    public RespBean getHeadTableData() {
        RespBean response = new RespBean();
        response.setStatus(200);
        try {
            AssetDashBoardHeadTableData data = assetService.getHeadTableData();
            response.setObj(data);
        } catch (Exception e) {
            logger.error("获取资产仪表盘数据异常！", e);
            return RespBean.error("获取仪表盘数据失败!");
        }
        return response;
    }
}
