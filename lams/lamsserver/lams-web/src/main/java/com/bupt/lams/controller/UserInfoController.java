package com.bupt.lams.controller;

import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 当前登录的用户相关
 */
@RestController
@RequestMapping("/user")
public class UserInfoController {

    @Autowired
    UserService userService;

    @GetMapping("/info")
    public LamsUser getCurrentUser(Authentication authentication) {
        LamsUser currentUser = (LamsUser) authentication.getPrincipal();
        return currentUser;
    }

    @PutMapping("/add")
    public RespBean addUser(@RequestBody LamsUser lamsUser) {
        if (userService.addUser(lamsUser) == 1) {
            return RespBean.ok("注册成功!");
        }
        return RespBean.error("注册失败!");
    }

    @PutMapping("/info")
    public RespBean updateUser(@RequestBody LamsUser lamsUser, Authentication authentication) {
        if (userService.updateUser(lamsUser) == 1) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(lamsUser, authentication.getCredentials(), authentication.getAuthorities()));
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @PutMapping("/pass")
    public RespBean updateUserPasswd(@RequestBody Map<String, Object> info) {
        String oldpass = (String) info.get("oldpass");
        String pass = (String) info.get("pass");
        Integer uid = (Integer) info.get("uid");
        if (userService.updateUserPasswd(oldpass, pass, uid)) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

}