package com.bupt.lams.mapper;

import com.bupt.lams.model.LamsUserRole;
import org.apache.ibatis.annotations.Param;

public interface LamsUserRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LamsUserRole record);

    int insertSelective(LamsUserRole record);

    LamsUserRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LamsUserRole record);

    int updateByPrimaryKey(LamsUserRole record);

    void deleteByUid(Integer uid);

    Integer addRole(@Param("uid") Integer uid, @Param("rids") Integer[] rids);
}