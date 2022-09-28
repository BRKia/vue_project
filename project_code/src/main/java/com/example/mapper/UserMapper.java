package com.example.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author brk
 * @since 2022-06-27
 */
public interface UserMapper extends BaseMapper<User> {

    @Select("select role from sys_user where nickname = #{nickname}")
    String selectRole(@Param("nickname") String nickname);

    Page<User> findPage01(Page<User> page,
                        @Param("username") String username,
                        @Param("nickname") String nickname,
                        @Param("email") String email,
                        @Param("address") String address);


    Page<User> findPage02(Page<User> page,
                          @Param("username") String username,
                          @Param("nickname") String nickname,
                          @Param("email") String email,
                          @Param("address") String address);
}
