//package com.bupt.workflow.utils;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.activiti.engine.identity.Group;
//import org.activiti.engine.impl.persistence.entity.GroupEntityImpl;
//import org.activiti.engine.impl.persistence.entity.UserEntity;
//import org.activiti.engine.impl.persistence.entity.UserEntityImpl;
//
//import com.sogou.admin.workorder.common.dto.AdminUserDto;
//import com.sogou.admin.workorder.common.dto.RoleDto;
//
///**
// * Activiti转换工具类
// *
// * @author shanglonghua
// *
// */
//public class ActivitiUtil {
//
//	/**
//	 * 将系统中User转换为Activiti相应的User对象
//	 *
//	 * @param customUser
//	 *            系统用户对象
//	 * @return Activiti用户实体对象
//	 */
//	public static UserEntity toActivitiUser(AdminUserDto customUser) {
//		UserEntityImpl userEntity = null;
//		if (customUser != null) {
//			userEntity = new UserEntityImpl();
//			userEntity.setId(customUser.getUserName());
//			userEntity.setFirstName(customUser.getUserName());
//			userEntity.setLastName(customUser.getRealName());
//			userEntity.setEmail(customUser.getUserEmail());
//			// 乐观锁版本，保持不变
//			userEntity.setRevision(1);
//		}
//		return userEntity;
//	}
//
//	/**
//	 * 将系统中角色Role转换为Activiti相应的用户组Group对象
//	 *
//	 * @param roleDto
//	 *            系统角色对象
//	 * @return Activiti角色实体对象
//	 */
//	private static Group toActivitiGroup(RoleDto roleDto) {
//		GroupEntityImpl group = null;
//		if (roleDto != null) {
//			group = new GroupEntityImpl();
//			group.setRevision(1);
//			// activiti中用户组分为两种类型 assignment和security-role
//			// assignment: 普通的岗位角色，是用户分配业务中的功能权限
//			// security-role: 安全角色，可以从全局管理用户组织及整个流程状态
//			// 目前系统中不设安全角色，默认设置为assignment
//			group.setType("assignment");
//			group.setId(roleDto.getRoleId().toString());
//			group.setName(roleDto.getRoleName());
//		}
//		return group;
//	}
//
//	/**
//	 * 批量转换为用户组
//	 *
//	 * @param roleDtos
//	 *            系统角色对象集合
//	 * @return Activiti角色实体对象
//	 */
//	public static List<Group> toActivitiGroups(List<RoleDto> roleDtos) {
//
//		List<Group> groupEntitys = new ArrayList<Group>();
//
//		for (RoleDto roleDto : roleDtos) {
//			Group group = toActivitiGroup(roleDto);
//			if (group != null)
//				groupEntitys.add(group);
//		}
//		return groupEntitys;
//	}
//
//}
