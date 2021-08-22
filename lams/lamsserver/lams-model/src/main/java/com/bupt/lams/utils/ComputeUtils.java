package com.bupt.lams.utils;

import org.apdplat.word.analysis.CosineTextSimilarity;
import org.apdplat.word.analysis.TextSimilarity;

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

    /**
     * 计算两个文本的相似度
     *
     * @param a
     * @param b
     * @return
     */
    public static double cos(String a, String b) {
        TextSimilarity textSimilarity = new CosineTextSimilarity();
        return textSimilarity.similarScore(a, b);
    }
}
