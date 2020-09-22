//package com.bupt.workflow.service.identity.impl;
//
//import com.bupt.workflow.utils.ActivitiUtil;
//import com.sogou.admin.workorder.adapter.service.auth.UserAdapterService;
//import com.sogou.admin.workorder.common.dto.AdminUserDto;
//import com.sogou.admin.workorder.common.utils.UserInfoUtils;
//import com.bupt.workflow.service.identity.GroupManager;
//import org.activiti.engine.identity.Group;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.util.List;
//
//@Service
//public class GroupManagerImpl implements GroupManager {
//
//	private Logger logger = LoggerFactory.getLogger(GroupManagerImpl.class);
//
//	@Resource
//	private UserAdapterService userAdapterService;
//
//	@Override
//	public Group get(Long id) {
//		throw new UnsupportedOperationException();
//	}
//
//	@Override
//	public List<Group> findByUserName(String username) {
//		logger.info("根据用户账号获取用户角色信息：" + username);
//		AdminUserDto userDto = UserInfoUtils.getLoginedAdminUser();
//		if(userDto==null || !userDto.getUserName().equalsIgnoreCase(username)){
//			userDto = this.userAdapterService.getUserByName(username);
//		}
//		List<Group> roleList = ActivitiUtil.toActivitiGroups(userDto.getRoleList());
//		return roleList;
//	}
//
//}
