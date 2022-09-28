package com.example.mapper;

import com.example.common.Result;
import com.example.entity.UserFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.FileDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author brk
 * @since 2022-08-11
 */
public interface UserFileMapper extends BaseMapper<UserFile> {
    @Delete("delete from sys_user_file where user_id = #{userId} and file_id = #{fileId}")
    int delete(@Param("userId") Integer userId, @Param("fileId") Integer fileId);

//    @Select("select user_id, download_time from sys_user_file where file_id = #{fileId}")
//    List<Object> selectByFileId(@Param("fileId") Integer fileId);

    // 根据文件id查询到文件信息
    @Select("select sys_user.nickname, sys_user_file.download_time as downloadTime from sys_user_file" +
            " left join sys_user " +
                "on sys_user_file.user_id = sys_user.id " +
            "where sys_user_file.file_id = #{fileId}")
    List<FileDTO> selectByFileIdResult(@Param("fileId") Integer fileId);



}
