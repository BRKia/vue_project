package com.example.service;

import com.example.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author brk
 * @since 2022-07-09
 */
public interface IMenuService extends IService<Menu> {

    List<Menu> findAll(String name);

    List<Menu> findMenus(String name);
}
