package com.bupt.workflow.service.identity.impl;

import com.bupt.workflow.service.identity.GroupManager;
import com.bupt.workflow.service.identity.UserManager;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.UserQueryImpl;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.persistence.entity.data.UserDataManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 自定义用户Service
 * 
 * @author shanglonghua
 * 
 */
@Service
public class UserDataCustomManager implements UserDataManager {

	@Resource
	private UserManager userManager;

	@Resource
	private GroupManager groupManager;

	@Override
	public UserEntity create() {
		throw new RuntimeException("not implements method");
	}

	@Override
	public void delete(String arg0) {
		throw new RuntimeException("not implements method");
	}

	@Override
	public void delete(UserEntity arg0) {
		throw new RuntimeException("not implements method");
	}

	@Override
	public UserEntity findById(String username) {
		if (username == null) {
			return null;
		}
		return userManager.get(username);
	}

	@Override
	public void insert(UserEntity arg0) {
		throw new RuntimeException("not implements method");
	}

	@Override
	public UserEntity update(UserEntity arg0) {
		throw new RuntimeException("not implements method");
	}

	@Override
	public List<Group> findGroupsByUser(String username) {
		if (username == null) {
			return null;
		}
		return groupManager.findByUserName(username);
	}

	@Override
	public List<User> findUserByQueryCriteria(UserQueryImpl arg0, Page arg1) {
		throw new RuntimeException("not implements method");
	}

	@Override
	public long findUserCountByNativeQuery(Map<String, Object> arg0) {
		throw new RuntimeException("not implements method");
	}

	@Override
	public long findUserCountByQueryCriteria(UserQueryImpl arg0) {
		throw new RuntimeException("not implements method");
	}

	@Override
	public List<User> findUsersByNativeQuery(Map<String, Object> arg0, int arg1, int arg2) {
		throw new RuntimeException("not implements method");
	}

}
