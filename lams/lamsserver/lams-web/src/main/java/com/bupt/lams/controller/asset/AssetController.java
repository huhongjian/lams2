package com.bupt.lams.controller.asset;

import com.bupt.lams.dto.AssetQueryCondition;
import com.bupt.lams.model.Asset;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.model.RespPageBean;
import com.bupt.lams.service.AssetService;
import com.bupt.lams.utils.UserInfoUtils;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/edit")
    public RespBean updateAsset(@RequestBody Asset asset) {
        // 资产信息管理中的资产信息只允许管理员修改
        if (UserInfoUtils.isAdmin() == false) {
            return RespBean.error("没有修改权限，请联系管理员!");
        }
        try {
            assetService.updateAsset(asset);
        } catch (Exception e) {
            return RespBean.error("更新失败!");
        }
        return RespBean.ok("更新成功!");
    }
}
