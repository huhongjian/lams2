package com.bupt.lams.mapper;

import com.bupt.lams.model.LamsUser;
import com.bupt.lams.model.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LamsUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LamsUser record);

    LamsUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LamsUser record);

    LamsUser loadUserByUsername(String username);

    List<Role> getRolesById(Integer id);

    List<Integer> getUserIdsByPage(@Param("uid") Integer uid, @Param("keywords") String keywords, @Param("page") Integer page, @Param("size") Integer size);

    List<LamsUser> getUsersByIds(@Param("ids") List<Integer> ids);

    Long getTotal(@Param("uid") Integer uid);

    List<LamsUser> getAllUsersExceptCurrent(Integer id);

    Integer updatePasswd(@Param("uid") Integer uid, @Param("encodePass") String encodePass);

    void enabledChangeByUsername(@Param("username") String username, @Param("enabled") Integer enabled);
}