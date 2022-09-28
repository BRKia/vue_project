package com.example.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.Menu;
import com.example.mapper.MenuMapper;
import com.example.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author brk
 * @since 2022-07-09
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {
    @Override
    public List<Menu> findAll(String name) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        //若名字为空，则不进行模糊查询。
        if(StrUtil.isNotBlank(name)){
            queryWrapper.like("name", name);
        }
        //查询所有数据
        List<Menu> list = list(queryWrapper);
        //找出pid为null的1级菜单
        List<Menu> parentNodes = list.stream().filter(menu -> menu.getPid() == null).collect(Collectors.toList());
        //找2级菜单
        for (Menu menu : parentNodes) {
            menu.setChildren(list.stream().filter(m -> menu.getId().equals(m.getPid())).collect(Collectors.toList()));
        }
        queryWrapper.orderByDesc("id");
        return parentNodes;
    }
    @Override
    public List<Menu> findMenus(String name) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        //若名字为空，则不进行模糊查询。
        if(StrUtil.isNotBlank(name)){
            queryWrapper.like("name", name);
        }
        //查询所有数据
        List<Menu> list = list(queryWrapper);
//        //找出pid为null的1级菜单
//        List<Menu> parentNodes = list.stream().filter(menu -> menu.getPid() == null).collect(Collectors.toList());
//        //找2级菜单
//        for (Menu menu : parentNodes) {
//            menu.setChildren(list.stream().filter(m -> menu.getId().equals(m.getPid())).collect(Collectors.toList()));
//        }
        queryWrapper.orderByDesc("id");
        return list;
    }
}
