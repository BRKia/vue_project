package com.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import com.example.service.IGameService;
import com.example.entity.Game;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author brk
 * @since 2022-07-24
 */
@RestController
@RequestMapping("/game")
public class GameController {

    @Resource
    private IGameService gameService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Game game) {
        gameService.saveOrUpdate(game);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        gameService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        gameService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(gameService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(gameService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Game> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return Result.success(gameService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}

