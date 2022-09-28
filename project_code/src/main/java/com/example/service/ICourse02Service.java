package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Course02;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author brk
 * @since 2022-07-16
 */
public interface ICourse02Service extends IService<Course02> {

    Page<Course02> findPage(Page<Course02> page, String name);
    void setStudentCourseId(Integer courseId, Integer studentId);
}
