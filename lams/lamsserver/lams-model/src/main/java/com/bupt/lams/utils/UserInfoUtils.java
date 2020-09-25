package com.bupt.lams.utils;

import com.bupt.lams.model.Hr;

/**
 * /**
 * 用户信息工具类
 */
public class UserInfoUtils {
    private static Hr currentUser;

    public static Hr getLoginedUser() {
        return currentUser;
    }

    public static void setContext(Hr user) {
        currentUser = user;
    }

    public static void clearContext() {
        currentUser = null;
    }
}
