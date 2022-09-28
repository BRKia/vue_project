package com.example.service.impl;

import com.example.entity.Course;
import com.example.mapper.CourseMapper;
import com.example.service.ICourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author brk
 * @since 2022-07-17
 */
@Service
        public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {
    @Resource
    private CourseMapper courseMapper;

    @Transactional
    @Override
    public void setStudentCourseId(Integer courseId, Integer studentId) {
        courseMapper.deleteStudentCourse(courseId, studentId); //先删除
        courseMapper.setStudentCourseId(courseId, studentId); //再插入
    }
}
