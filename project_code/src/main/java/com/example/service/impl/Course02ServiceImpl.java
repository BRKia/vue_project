package com.example.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Course02;
import com.example.mapper.Course02Mapper;
import com.example.mapper.CourseMapper;
import com.example.service.ICourse02Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author brk
 * @since 2022-07-16
 */
@Service
        public class Course02ServiceImpl extends ServiceImpl<Course02Mapper, Course02> implements ICourse02Service {

        @Override
        public Page<Course02> findPage(Page<Course02> page, String name) {
                return course02Mapper.findPage(page, name);
        }

        @Resource
        private Course02Mapper course02Mapper;

        @Transactional
        @Override
        public void setStudentCourseId(Integer courseId, Integer studentId) {
                course02Mapper.deleteStudentCourse(courseId, studentId); //先删除
                course02Mapper.setStudentCourseId(courseId, studentId); //再插入
        }
}
