package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.mapper.MaterialFeeMapper;
import com.bupt.lams.model.Project;
import com.bupt.lams.model.RespPageBean;
import com.bupt.lams.model.planItem.EquipmentFee;
import com.bupt.lams.model.planItem.MaterialFee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 设备费service
 */
@Service
public class MaterialFeeService {
    @Resource
    MaterialFeeMapper materialFeeMapper;

    public List<MaterialFee> getMaterialFeeList() {
        return materialFeeMapper.getMaterialFeeList();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long addMaterialFee(MaterialFee materialFee) {
        materialFee.setItemType(PlanItemTypeEnum.MATERIAL.getIndex());
        // 计算合计
        if (materialFee.getSubTotal() == null) {
            materialFee.setSubTotal(materialFee.getUnitPrice() * materialFee.getAmount());
        }
        materialFeeMapper.insertSelective(materialFee);
        return materialFee.getId();
    }

    public void updateAsset(Project project) {
        materialFeeMapper.updateProject(project);
    }

    /**
     * 将结果集转化为展示集合
     *
     * @param page
     * @param size
     * @param data
     * @return
     */
    private RespPageBean transToRespPageBean(Integer page, Integer size, List<Project> data) {
        Long total = Long.valueOf(data.size());
        Integer end = Math.min(page + size, data.size());
        // 展示的数据
        List<Project> res = new ArrayList<>();
        for (Integer i = page; i < end; i++) {
            res.add(data.get(i));
        }
        RespPageBean bean = new RespPageBean();
        bean.setData(res);
        bean.setTotal(total);
        return bean;
    }
}
