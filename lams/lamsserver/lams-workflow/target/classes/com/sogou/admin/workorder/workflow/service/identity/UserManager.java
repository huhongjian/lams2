package com.sogou.admin.workorder.workflow.service.identity;

import org.activiti.engine.impl.persistence.entity.UserEntity;

public interface UserManager {

	/**
	 * 根据用户账号查询用户
	 *
	 * @param username
	 *            用户账号
	 * @return 用户信息
	 */
	UserEntity get(String username);

}
