package com.bupt.lams.mapper;

import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LamsUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LamsUser record);

    int insertSelective(LamsUser record);

    LamsUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LamsUser record);

    int updateByPrimaryKey(LamsUser record);

    LamsUser loadUserByUsername(String username);

    List<Role> getHrRolesById(Integer id);

    List<LamsUser> getAllHrs(@Param("uid") Integer uid, @Param("keywords") String keywords);

    List<LamsUser> getAllHrsExceptCurrentHr(Integer id);

    Integer updatePasswd(@Param("uid") Integer hrid, @Param("encodePass") String encodePass);

    Integer updateUserface(@Param("url") String url, @Param("id") Integer id);
}