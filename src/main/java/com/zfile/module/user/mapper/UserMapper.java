package com.zfile.module.user.mapper;

import com.zfile.module.user.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author zhang
* @description 针对表【user(用户表)】的数据库操作Mapper
* @createDate 2025-11-07 17:55:31
* @Entity com.zfile.module.user.entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT id,username,nickname,password,enable,create_time,update_time,default_permissions,salt FROM user WHERE username = #{username}")
    @ResultMap("BaseResultMap")
    User getUserByUsername(String username);

    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 用户信息
     */
    default User selectByUsername(String username) {
        return getUserByUsername(username);
    }

    @Select("SELECT id,username,nickname,password,enable,create_time,update_time,default_permissions,salt FROM user WHERE default_permissions = #{permissions}")
    @ResultMap("BaseResultMap")
    List<User> listUsersByPermissions(String permissions);

    @Select("SELECT id,username,nickname,password,enable,create_time,update_time,default_permissions,salt FROM user WHERE enable = #{enable}")
    @ResultMap("BaseResultMap")
    List<User> listUsersByEnableStatus(Integer enable);
}