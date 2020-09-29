package com.bupt.lams.controller;

import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.RespBean;
import com.bupt.lams.service.HrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 当前登录的用户相关
 */
@RestController
public class HrInfoController {

    @Autowired
    HrService hrService;

    @Value("${fastdfs.nginx.host}")
    String nginxHost;

    @GetMapping("/hr/info")
    public LamsUser getCurrentHr(Authentication authentication) {
        LamsUser currentUser = (LamsUser) authentication.getPrincipal();
        return currentUser;
    }

    @PutMapping("/user/add")
    public RespBean addUser(@RequestBody LamsUser lamsUser) {
        if (hrService.addUser(lamsUser) == 1) {
            return RespBean.ok("注册成功!");
        }
        return RespBean.error("注册失败!");
    }

    @PutMapping("/hr/info")
    public RespBean updateHr(@RequestBody LamsUser lamsUser, Authentication authentication) {
        if (hrService.updateHr(lamsUser) == 1) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(lamsUser, authentication.getCredentials(), authentication.getAuthorities()));
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @PutMapping("/user/pass")
    public RespBean updateHrPasswd(@RequestBody Map<String, Object> info) {
        String oldpass = (String) info.get("oldpass");
        String pass = (String) info.get("pass");
        Integer hrid = (Integer) info.get("hrid");
        if (hrService.updateHrPasswd(oldpass, pass, hrid)) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

}