package com.example.mapper;

import com.example.entity.Files;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author brk
 * @since 2022-08-06
 */
public interface FileMapper extends BaseMapper<Files> {
    @Select("select name from sys_file where id = #{id}")
    String selectByFileId(@Param("id") Integer id);


    @Update("update sys_user set avatar = #{new_name} where avatar like #{old_name}")
    void updateFileAvatar(@Param("old_name") String old_name, @Param("new_name") String new_name);
    // update sys_user set avatar = null where avatar like '%9090%'
    // http://124.70.63.245:9090/file/

    @Update("update sys_file set url = #{new_url} where url like #{old_url}")
    void updateUrl(@Param("old_url") String old_url, @Param("new_url") String new_url);
}
