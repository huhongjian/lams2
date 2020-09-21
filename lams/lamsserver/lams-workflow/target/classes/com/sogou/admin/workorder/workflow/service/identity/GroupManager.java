package com.sogou.admin.workorder.workflow.service.identity;

import java.util.List;

import org.activiti.engine.identity.Group;

/**
 * 用户查询
 *
 * @author shanglonghua
 */
public interface GroupManager {

	/**
	 * 根据ID查询角色(用户组)
	 *
	 * @param id
	 *            用户ID
	 * @return 角色/用户组
	 */
	Group get(Long id);

	/**
	 * 根据用户账号查询拥有的角色(用户组)
	 *
	 * @param username
	 *            用户账号
	 * @return 用户对应的角色/用户组集合
	 */
	List<Group> findByUserName(String username);

}
