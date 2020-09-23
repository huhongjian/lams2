package com.bupt.lams.service.identity.impl;

import com.bupt.lams.model.Hr;
import com.bupt.lams.service.identity.GroupManager;
import com.bupt.lams.utils.ActivitiUtil;
import com.bupt.lams.utils.UserInfoUtils;
import org.activiti.engine.identity.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupManagerImpl implements GroupManager {

	private Logger logger = LoggerFactory.getLogger(GroupManagerImpl.class);

	@Override
	public Group get(Long id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Group> findByUserName(String username) {
		logger.info("根据用户账号获取用户角色信息：" + username);
		Hr user = UserInfoUtils.getLoginedUser();
		List<Group> roleList = ActivitiUtil.toActivitiGroups(user.getRoles());
		return roleList;
	}
}
