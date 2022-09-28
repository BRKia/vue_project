package com.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.dto.FileDTO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import com.example.service.IUserFileService;
import com.example.entity.UserFile;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author brk
 * @since 2022-08-11
 */
@RestController
@RequestMapping("/userFile")
public class UserFileController {

    @Resource
    private IUserFileService userFileService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody UserFile userFile) {
        userFileService.saveOrUpdate(userFile);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        userFileService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        userFileService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(userFileService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(userFileService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<UserFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return Result.success(userFileService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    // 一个文件id查出多个用户id
    @GetMapping("/checkDownload/{fileId}")
    public Result checkDownload(@PathVariable Integer fileId) {
//        QueryWrapper<FileDTO> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("file_id", fileId);
//        userFileService.list(queryWrapper);
        return Result.success(userFileService.checkDownload(fileId));
    }

    /**
     * 一个用户下载多个文件
     * @param userId 用户id
     * @param fileId 文件ids
     * @return
     */
    @PostMapping("/postDownload/{userId}")
    public Result postDownload(@PathVariable Integer userId, @RequestBody Integer fileId) {
        userFileService.setUserFile(userId, fileId);
        return Result.success();
    }

}

