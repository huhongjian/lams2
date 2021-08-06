package com.bupt.lams.service;

import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.mapper.EquipmentFeeMapper;
import com.bupt.lams.model.planItem.EquipmentFee;
import com.bupt.lams.model.Project;
import com.bupt.lams.model.RespPageBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 设备费service
 */
@Service
public class EquipmentFeeService {
    @Resource
    EquipmentFeeMapper equipmentFeeMapper;

    public List<EquipmentFee> getEquipmentFeeList() {
        return equipmentFeeMapper.getEquipmentFeeList();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long addEquipmentFee(EquipmentFee equipmentFee) {
        equipmentFee.setItemType(PlanItemTypeEnum.EQUIPMENT.getIndex());
        // 计算合计
        equipmentFee.setSubTotal(equipmentFee.getUnitPrice() * equipmentFee.getAmount());
        equipmentFeeMapper.insertSelective(equipmentFee);
        return equipmentFee.getId();
    }

    public void updateAsset(Project project) {
        equipmentFeeMapper.updateProject(project);
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
