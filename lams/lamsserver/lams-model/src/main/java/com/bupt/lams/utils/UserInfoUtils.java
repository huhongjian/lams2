package com.bupt.lams.utils;

import com.bupt.lams.model.LamsUser;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * /**
 * 用户信息工具类
 */
public class UserInfoUtils {
    public static LamsUser getLoginedUser() {
        return ((LamsUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
}
