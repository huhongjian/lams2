package com.bupt.lams.controller.asset;

import com.bupt.lams.dto.AssetQueryCondition;
import com.bupt.lams.model.RespPageBean;
import com.bupt.lams.service.AssetService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 工单信息相关
 */
@RestController
@RequestMapping("/asset")
public class AssetController {
    @Resource
    AssetService assetService;

    @GetMapping("/get")
    public RespPageBean getAssetInfoByPage(AssetQueryCondition assetQueryCondition) {
        return assetService.getAssetByCondition(assetQueryCondition);
    }
}
