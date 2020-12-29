package com.bupt.lams.constants;

import java.util.HashMap;
import java.util.Map;

public class TaskHandleDispatch {
    public static Map<Integer, String> taskHandleMap;

    static {
        taskHandleMap = new HashMap<>();
        taskHandleMap.put(2, "handleApprove");
        taskHandleMap.put(3, "handleReject");
        taskHandleMap.put(4, "handleAssetIn");
        taskHandleMap.put(6, "handleConfirm");
        taskHandleMap.put(8, "handleRefuse");
        taskHandleMap.put(11, "handleStuOutApprove");
        taskHandleMap.put(12, "handleStuOutReject");
    }
}
