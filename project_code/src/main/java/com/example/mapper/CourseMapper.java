package com.example.mapper;

import com.example.entity.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author brk
 * @since 2022-07-17
 */
public interface CourseMapper extends BaseMapper<Course> {

    void setStudentCourseId(@Param("courseId") Integer courseId, @Param("studentId") Integer studentId);

    void deleteStudentCourse(@Param("courseId") Integer courseId, @Param("studentId") Integer studentId);
}
