package com.bupt.lams.utils;

import com.bupt.lams.model.Hr;
import org.springframework.util.Assert;

/**
 * /**
 * 用户信息工具类
 */
public class UserInfoUtils {
    private static ThreadLocal<Hr> userInfoContext = new ThreadLocal<Hr>();

    public static Hr getLoginedUser() {
        return userInfoContext.get();
    }

    public static void setContext(Hr userDto) {
        Assert.notNull(userDto, "Only non-null UserInfo instances are permitted");
        userInfoContext.set(userDto);
    }

    public static void clearContext() {
        userInfoContext.set(null);
    }
}
