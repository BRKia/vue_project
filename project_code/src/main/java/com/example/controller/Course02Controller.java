package com.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Course;
import com.example.entity.User;
import com.example.service.IUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.example.common.Result;

import com.example.service.ICourse02Service;
import com.example.entity.Course02;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author brk
 * @since 2022-07-16
 */
@RestController
@RequestMapping("/course02")
public class Course02Controller {

    @Resource
    private ICourse02Service course02Service;

    @Resource
    private IUserService userService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Course02 course02) {
        course02Service.saveOrUpdate(course02);
        return Result.success();
    }

    @PostMapping("/studentCourse/{studentId}/{courseId}")
    public Result studentCourse(@PathVariable Integer courseId, @PathVariable Integer studentId) {
        course02Service.setStudentCourseId(courseId, studentId);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        course02Service.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        course02Service.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(course02Service.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(course02Service.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name) {
//        QueryWrapper<Course02> queryWrapper = new QueryWrapper<>();
//        if (!"".equals(name)) {
//            name = "%" + name + "%";
//            queryWrapper.like("name", name);
//        }
//        queryWrapper.orderByAsc("id");
        Page<Course02> page = course02Service.findPage(new Page<>(pageNum, pageSize), name);
        return Result.success(page);
    }

}

