package com.bupt.lams.utils;

import java.text.DecimalFormat;

public class ComputeUtils {
    public static Double computeExecuteRate(Double total, Double unfulfilledQuota) {
        DecimalFormat df = new DecimalFormat("#.00");
        Double executeRate = (total - unfulfilledQuota) / total * 100;
        return new Double(df.format(executeRate.doubleValue()));
    }
}
