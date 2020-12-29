package com.bupt.lams.controller.asset;

import com.bupt.lams.model.AssetType;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.service.AssetTypesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 资产类型
 */
@RestController
@RequestMapping("/asset/types")
public class AssetTypeController {
    private Logger logger = LoggerFactory.getLogger(AssetTypeController.class);
    @Resource
    AssetTypesService assetTypesService;

    @GetMapping("/get")
    public RespBean getAssetTypes() {
        RespBean response = new RespBean();
        response.setStatus(200);
        try {
            List<AssetType> types = assetTypesService.getAllAssetTypes();
            response.setObj(types);
        } catch (Exception e) {
            logger.error("获取资产类型失败", e);
            return RespBean.error("获取资产类型失败！");
        }
        return response;
    }
}
