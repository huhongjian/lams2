package com.sogou.admin.workorder.workflow.service.identity.impl;

import com.sogou.admin.workorder.adapter.service.auth.UserAdapterService;
import com.sogou.admin.workorder.common.dto.AdminUserDto;
import com.sogou.admin.workorder.common.utils.UserInfoUtils;
import com.sogou.admin.workorder.workflow.service.identity.UserManager;
import com.sogou.admin.workorder.workflow.utils.ActivitiUtil;
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
