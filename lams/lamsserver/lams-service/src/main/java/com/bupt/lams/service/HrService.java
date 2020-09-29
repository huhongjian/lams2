package com.bupt.lams.service;

import com.bupt.lams.model.LamsUser;
import com.bupt.lams.mapper.LamsUserMapper;
import com.bupt.lams.mapper.LamsUserRoleMapper;
import com.bupt.lams.utils.HrUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class HrService implements UserDetailsService {
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
        lamsUser.setRoles(lamsUserMapper.getHrRolesById(lamsUser.getId()));
        return lamsUser;
    }

    public List<LamsUser> getAllHrs(String keywords) {
        return lamsUserMapper.getAllHrs(HrUtils.getCurrentHr().getId(), keywords);
    }

    public Integer updateHr(LamsUser lamsUser) {
        return lamsUserMapper.updateByPrimaryKeySelective(lamsUser);
    }

    @Transactional
    public boolean updateHrRole(Integer hrid, Integer[] rids) {
        lamsUserRoleMapper.deleteByHrid(hrid);
        return lamsUserRoleMapper.addRole(hrid, rids) == rids.length;
    }

    public Integer deleteHrById(Integer id) {
        return lamsUserMapper.deleteByPrimaryKey(id);
    }

    public List<LamsUser> getAllHrsExceptCurrentHr() {
        return lamsUserMapper.getAllHrsExceptCurrentHr(HrUtils.getCurrentHr().getId());
    }

    public Integer updateHyById(LamsUser lamsUser) {
        return lamsUserMapper.updateByPrimaryKeySelective(lamsUser);
    }

    public boolean updateHrPasswd(String oldpass, String pass, Integer hrid) {
        LamsUser lamsUser = lamsUserMapper.selectByPrimaryKey(hrid);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(oldpass, lamsUser.getPassword())) {
            String encodePass = encoder.encode(pass);
            Integer result = lamsUserMapper.updatePasswd(hrid, encodePass);
            if (result == 1) {
                return true;
            }
        }
        return false;
    }

    public Integer updateUserface(String url, Integer id) {
        return lamsUserMapper.updateUserface(url, id);
    }
}
