package com.bupt.lams.service.identity.impl;

import com.bupt.lams.model.LamsUser;
import com.bupt.lams.service.identity.UserManager;
import com.bupt.lams.utils.ActivitiUtil;
import com.bupt.lams.utils.UserInfoUtils;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class UserManagerImpl implements UserManager {

    @Override
    public UserEntity get(String username) {
        LamsUser userDto = UserInfoUtils.getLoginedUser();
        return ActivitiUtil.toActivitiUser(userDto);
    }
}
