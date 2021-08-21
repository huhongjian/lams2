package com.bupt.lams.utils;

import org.apache.commons.collections4.SetUtils;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

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
        if (a == null || b == null) {
            return 0d;
        }
        Set<Integer> aChar = a.chars().boxed().collect(Collectors.toSet());
        Set<Integer> bChar = b.chars().boxed().collect(Collectors.toSet());

        // 统计字频
        Map<Integer, Integer> aMap = new HashMap<>();
        Map<Integer, Integer> bMap = new HashMap<>();
        for (Integer a1 : aChar) {
            aMap.put(a1, aMap.getOrDefault(a1, 0) + 1);
        }
        for (Integer b1 : bChar) {
            bMap.put(b1, bMap.getOrDefault(b1, 0) + 1);
        }

        // 向量化
        Set<Integer> union = SetUtils.union(aChar, bChar);
        int[] aVec = new int[union.size()];
        int[] bVec = new int[union.size()];
        List<Integer> collect = new ArrayList<>(union);
        for (int i = 0; i < collect.size(); i++) {
            aVec[i] = aMap.getOrDefault(collect.get(i), 0);
            bVec[i] = bMap.getOrDefault(collect.get(i), 0);
        }

        // 分别计算三个参数
        int p1 = 0;
        for (int i = 0; i < aVec.length; i++) {
            p1 += (aVec[i] * bVec[i]);
        }

        double p2 = 0d;
        for (int i : aVec) {
            p2 += (i * i);
        }
        p2 = Math.sqrt(p2);

        double p3 = 0d;
        for (int i : bVec) {
            p3 += (i * i);
        }
        p3 = Math.sqrt(p3);

        return ((double) p1) / (p2 * p3);
    }
}
