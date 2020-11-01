package com.bupt.lams.service;

import com.bupt.lams.mapper.LamsUserMapper;
import com.bupt.lams.mapper.LamsUserRoleMapper;
import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.RespPageBean;
import com.bupt.lams.utils.UserInfoUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户server
 */
@Service
public class UserService implements UserDetailsService {
    @Resource
    LamsUserMapper lamsUserMapper;
    @Resource
    LamsUserRoleMapper lamsUserRoleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LamsUser lamsUser = lamsUserMapper.loadUserByUsername(username);
        if (lamsUser == null) {
            throw new UsernameNotFoundException("用户名不存在!");
        }
        lamsUser.setRoles(lamsUserMapper.getRolesById(lamsUser.getId()));
        return lamsUser;
    }

    public RespPageBean getUsersByPage(String keywords, Integer page, Integer size) {
        Integer uid = UserInfoUtils.getLoginedUser().getId();
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Integer> ids = lamsUserMapper.getUserIdsByPage(uid, keywords, page, size);
        List<LamsUser> data = lamsUserMapper.getUsersByIds(ids);
        Long total = lamsUserMapper.getTotal(uid);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer addUser(LamsUser lamsUser) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        lamsUser.setPassword(encoder.encode(lamsUser.getPassword()));
        Integer res = lamsUserMapper.insert(lamsUser);
        lamsUserRoleMapper.addRole(lamsUser.getId(), new Integer[]{24});
        return res;
    }

    public Integer updateUser(LamsUser lamsUser) {
        return lamsUserMapper.updateByPrimaryKeySelective(lamsUser);
    }

    @Transactional
    public void updateUserRole(Integer uid, Integer[] rids) {
        lamsUserRoleMapper.deleteByUid(uid);
        if (rids != null && rids.length > 0) {
            lamsUserRoleMapper.addRole(uid, rids);
        }
    }

    public Integer deleteUserById(Integer id) {
        return lamsUserMapper.deleteByPrimaryKey(id);
    }

    public List<LamsUser> getAllUsersExceptCurrent() {
        return lamsUserMapper.getAllUsersExceptCurrent(UserInfoUtils.getLoginedUser().getId());
    }

    public Integer updateUserById(LamsUser lamsUser) {
        return lamsUserMapper.updateByPrimaryKeySelective(lamsUser);
    }

    public boolean updateUserPasswd(String oldpass, String pass, Integer uid) {
        LamsUser lamsUser = lamsUserMapper.selectByPrimaryKey(uid);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(oldpass, lamsUser.getPassword())) {
            String encodePass = encoder.encode(pass);
            Integer result = lamsUserMapper.updatePasswd(uid, encodePass);
            if (result == 1) {
                return true;
            }
        }
        return false;
    }

    public LamsUser selectByPrimaryKey(Integer id) {
        return lamsUserMapper.selectByPrimaryKey(id);
    }

    public LamsUser getUserByUsername(String username) {
        return lamsUserMapper.loadUserByUsername(username);
    }
}
