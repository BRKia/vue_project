package com.example.service;

import com.example.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author brk
 * @since 2022-07-17
 */
public interface ICourseService extends IService<Course> {

    void setStudentCourseId(Integer courseId, Integer studentId);
}
