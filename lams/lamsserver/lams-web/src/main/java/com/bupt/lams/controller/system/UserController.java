package com.bupt.lams.controller.system;

import com.bupt.lams.model.*;
import com.bupt.lams.service.UserService;
import com.bupt.lams.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理
 */
@RestController
@RequestMapping("/system/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;

    @GetMapping("/")
    public RespPageBean getAllUsersByPage(String keywords, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        return userService.getUsersByPage(keywords, page, size);
    }

    @PutMapping("/")
    public RespBean updateHr(@RequestBody LamsUser lamsUser) {
        if (userService.updateUser(lamsUser) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @PutMapping("/role")
    public RespBean updateUserRole(Integer uid, Integer[] rids) {
        try {
            userService.updateUserRole(uid, rids);
        } catch (Exception e) {
            logger.error("更新用户角色信息失败！", e);
            return RespBean.error("更新失败!");
        }
        return RespBean.ok("更新成功!");
    }

    @DeleteMapping("/{id}")
    public RespBean deleteUserById(@PathVariable Integer id) {
        if (userService.deleteUserById(id) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }
}
