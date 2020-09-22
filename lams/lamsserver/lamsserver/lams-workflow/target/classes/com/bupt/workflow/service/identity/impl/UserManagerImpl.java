package com.bupt.workflow.service.identity.impl;

import com.bupt.workflow.utils.ActivitiUtil;
import com.sogou.admin.workorder.adapter.service.auth.UserAdapterService;
import com.sogou.admin.workorder.common.dto.AdminUserDto;
import com.sogou.admin.workorder.common.utils.UserInfoUtils;
import com.bupt.workflow.service.identity.UserManager;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserManagerImpl implements UserManager {

	@Resource
	private UserAdapterService userAdapterService;

	@Override
	public UserEntity get(String username) {
		AdminUserDto userDto = UserInfoUtils.getLoginedAdminUser();
		if (userDto == null || !userDto.getUserName().equalsIgnoreCase(username)) {
			userDto = this.userAdapterService.getUserByName(username);
		}
		return ActivitiUtil.toActivitiUser(userDto);
	}

}