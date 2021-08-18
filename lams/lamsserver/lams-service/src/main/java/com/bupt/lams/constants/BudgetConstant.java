package com.bupt.lams.constants;

import com.bupt.lams.model.planItem.*;
import com.bupt.lams.service.*;

import java.util.HashMap;
import java.util.Map;

public class BudgetConstant {
    public static Map<String, Class> PlanItemMap = new HashMap<String, Class>() {
        {
            put("academicFeeData", AcademicFeeService.class);
            put("advisoryData", AdvisoryService.class);
            put("equipmentFeeData", EquipmentFeeService.class);
            put("materialFeeData", MaterialFeeService.class);
            put("meetingFeeData", MeetingFeeService.class);
            put("otherFeeData", OtherFeeService.class);
            put("paymentData", PaymentService.class);
            put("transFeeData", TransportationFeeService.class);
            put("publishFeeData", PublishFeeService.class);
            put("researchFeeData", ResearchFeeService.class);
            put("testingFeeData", TestingFeeService.class);
        }
    };

    public static Map<Integer, Class> ItemTypeMap = new HashMap<Integer, Class>() {
        {
            put(PlanItemTypeEnum.ACADEMIC.getIndex(), AcademicFeeService.class);
            put(PlanItemTypeEnum.ADVISORY.getIndex(), AdvisoryService.class);
            put(PlanItemTypeEnum.EQUIPMENT.getIndex(), EquipmentFeeService.class);
            put(PlanItemTypeEnum.MATERIAL.getIndex(), MaterialFeeService.class);
            put(PlanItemTypeEnum.MEETING.getIndex(), MeetingFeeService.class);
            put(PlanItemTypeEnum.OTHERS.getIndex(), OtherFeeService.class);
            put(PlanItemTypeEnum.PAYMENT.getIndex(), PaymentService.class);
            put(PlanItemTypeEnum.TRANSPORTATION.getIndex(), TransportationFeeService.class);
            put(PlanItemTypeEnum.PUBLISH.getIndex(), PublishFeeService.class);
            put(PlanItemTypeEnum.RESEARCH.getIndex(), ResearchFeeService.class);
            put(PlanItemTypeEnum.TESTING.getIndex(), TestingFeeService.class);
        }
    };

    public static Map<Integer, String> ItemTypeDataMap = new HashMap<Integer, String>() {
        {
            put(PlanItemTypeEnum.ACADEMIC.getIndex(), "academicFeeData");
            put(PlanItemTypeEnum.ADVISORY.getIndex(), "advisoryData");
            put(PlanItemTypeEnum.EQUIPMENT.getIndex(), "equipmentFeeData");
            put(PlanItemTypeEnum.MATERIAL.getIndex(), "materialFeeData");
            put(PlanItemTypeEnum.MEETING.getIndex(), "meetingFeeData");
            put(PlanItemTypeEnum.OTHERS.getIndex(), "otherFeeData");
            put(PlanItemTypeEnum.PAYMENT.getIndex(), "paymentData");
            put(PlanItemTypeEnum.TRANSPORTATION.getIndex(), "transFeeData");
            put(PlanItemTypeEnum.PUBLISH.getIndex(), "publishFeeData");
            put(PlanItemTypeEnum.RESEARCH.getIndex(), "researchFeeData");
            put(PlanItemTypeEnum.TESTING.getIndex(), "testingFeeData");
        }
    };

    public static Map<Integer, Class> ItemTypePojoMap = new HashMap<Integer, Class>() {
        {
            put(PlanItemTypeEnum.ACADEMIC.getIndex(), AcademicFee.class);
            put(PlanItemTypeEnum.ADVISORY.getIndex(), Advisory.class);
            put(PlanItemTypeEnum.EQUIPMENT.getIndex(), EquipmentFee.class);
            put(PlanItemTypeEnum.MATERIAL.getIndex(), MaterialFee.class);
            put(PlanItemTypeEnum.MEETING.getIndex(), MeetingFee.class);
            put(PlanItemTypeEnum.OTHERS.getIndex(), OtherFee.class);
            put(PlanItemTypeEnum.PAYMENT.getIndex(), Payment.class);
            put(PlanItemTypeEnum.TRANSPORTATION.getIndex(), PlanItem.class);
            put(PlanItemTypeEnum.PUBLISH.getIndex(), PublishFee.class);
            put(PlanItemTypeEnum.RESEARCH.getIndex(), ResearchFee.class);
            put(PlanItemTypeEnum.TESTING.getIndex(), TestingFee.class);
        }
    };
}
