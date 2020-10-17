package com.bupt.lams.utils;

import java.util.ArrayList;
import java.util.List;

import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.Role;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.GroupEntityImpl;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.persistence.entity.UserEntityImpl;

/**
 * Activiti转换工具类
 */
public class ActivitiUtil {

    /**
     * 将系统中User转换为Activiti相应的User对象
     *
     * @param lamsUser 系统用户对象
     * @return Activiti用户实体对象
     */
    public static UserEntity toActivitiUser(LamsUser lamsUser) {
        UserEntityImpl userEntity = null;
        if (lamsUser != null) {
            userEntity = new UserEntityImpl();
            userEntity.setId(lamsUser.getUsername());
            userEntity.setFirstName(lamsUser.getUsername());
            userEntity.setLastName(lamsUser.getName());
            userEntity.setEmail(lamsUser.getUsername());
            // 乐观锁版本，保持不变
            userEntity.setRevision(1);
        }
        return userEntity;
    }

    /**
     * 将系统中角色Role转换为Activiti相应的用户组Group对象
     *
     * @param role 系统角色对象
     * @return Activiti角色实体对象
     */
    private static Group toActivitiGroup(Role role) {
        GroupEntityImpl group = null;
        if (role != null) {
            group = new GroupEntityImpl();
            group.setRevision(1);
            // activiti中用户组分为两种类型 assignment和security-role
            // assignment: 普通的岗位角色，是用户分配业务中的功能权限
            // security-role: 安全角色，可以从全局管理用户组织及整个流程状态
            // 目前系统中不设安全角色，默认设置为assignment
            group.setType("assignment");
            group.setId(role.getId().toString());
            group.setName(role.getName());
        }
        return group;
    }

    /**
     * 批量转换为用户组
     *
     * @param roles 系统角色对象集合
     * @return Activiti角色实体对象
     */
    public static List<Group> toActivitiGroups(List<Role> roles) {

        List<Group> groupEntitys = new ArrayList<>();

        for (Role r : roles) {
            Group group = toActivitiGroup(r);
            if (group != null)
                groupEntitys.add(group);
        }
        return groupEntitys;
    }

}
