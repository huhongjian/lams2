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

    List<Integer> getUserIdsByPage(@Param("uid") Integer uid, @Param("keywords") String keywords, @Param("page") Integer page, @Param("size") Integer size);

    List<LamsUser> getUsersByIds(@Param("ids") List<Integer> ids);

    Long getTotal(@Param("uid") Integer uid);

    List<LamsUser> getAllUsersExceptCurrentHr(Integer id);

    Integer updatePasswd(@Param("uid") Integer hrid, @Param("encodePass") String encodePass);
}