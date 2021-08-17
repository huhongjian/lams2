package com.bupt.lams.constants;

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
}
