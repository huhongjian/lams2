package com.bupt.lams.service.identity.impl;

import com.bupt.lams.service.identity.GroupManager;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.GroupQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.data.GroupDataManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 自定义角色Service
 * 
 * @author shanglonghua
 * 
 */
@Service
public class GroupDataCustomManager implements GroupDataManager {

	@Resource
	private GroupManager groupManager;

	@Override
	public List<Group> findGroupByQueryCriteria(GroupQueryImpl query, Page page) {
		String username = query.getUserId();
		if (username == null) {
			return null;
		}
		List<Group> groupList = groupManager.findByUserName(username);
		return groupList;
	}

	@Override
	public List<Group> findGroupsByUser(String username) {
		return groupManager.findByUserName(username);
	}

	@Override
	public GroupEntity create() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(GroupEntity arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public GroupEntity findById(String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void insert(GroupEntity arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public GroupEntity update(GroupEntity arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public long findGroupCountByNativeQuery(Map<String, Object> arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public long findGroupCountByQueryCriteria(GroupQueryImpl arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Group> findGroupsByNativeQuery(Map<String, Object> arg0, int arg1, int arg2) {
		throw new UnsupportedOperationException();
	}


}
