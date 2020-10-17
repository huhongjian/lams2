package com.bupt.lams.utils;

import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.Role;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

/**
 * 用户信息工具类
 */
public class UserInfoUtils {
    /**
     * 获取当前登录的用户
     *
     * @return
     */
    public static LamsUser getLoginedUser() {
        return ((LamsUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    /**
     * 当前用户是否有管理员权限
     *
     * @return
     */
    public static boolean isAdmin() {
        LamsUser user = UserInfoUtils.getLoginedUser();
        List<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (role.getName().equals("ROLE_admin")) {
                return true;
            }
        }
        return false;
    }
}
