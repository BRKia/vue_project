package com.example.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Course02;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author brk
 * @since 2022-07-16
 */
public interface Course02Mapper extends BaseMapper<Course02> {


    void setStudentCourseId(@Param("courseId") Integer courseId, @Param("studentId") Integer studentId);

    void deleteStudentCourse(@Param("courseId") Integer courseId, @Param("studentId") Integer studentId);

    Page<Course02> findPage(Page<Course02> page, String name);
}
