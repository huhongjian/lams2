package com.bupt.lams.utils;

import java.text.DecimalFormat;

public class ComputeUtils {
    public static Double computeExecuteRate(Double total, Double unfulfilledQuota) {
        if (total == 0) {
            return 0D;
        }
        DecimalFormat df = new DecimalFormat("#.00");
        Double executeRate = (total - unfulfilledQuota) / total * 100;
        return new Double(df.format(executeRate.doubleValue()));
    }
}
