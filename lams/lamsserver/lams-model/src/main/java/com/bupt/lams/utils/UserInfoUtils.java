package com.bupt.lams.utils;

import com.bupt.lams.model.LamsUser;

/**
 * /**
 * 用户信息工具类
 */
public class UserInfoUtils {
    private static LamsUser currentUser;

    public static LamsUser getLoginedUser() {
        return currentUser;
    }

    public static void setContext(LamsUser user) {
        currentUser = user;
    }

    public static void clearContext() {
        currentUser = null;
    }
}
