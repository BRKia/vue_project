package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.Menu;
import com.example.entity.Role;
import com.example.entity.RoleMenu;
import com.example.mapper.RoleMapper;
import com.example.mapper.RoleMenuMapper;
import com.example.service.IMenuService;
import com.example.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author brk
 * @since 2022-07-09
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private IMenuService menuService;

    @Transactional
    @Override
    public void setRoleMenu(Integer roleId, List<Integer> menuIds) {
        /*  比较冗杂的方法
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        roleMenuMapper.delete(queryWrapper);
         */
        roleMenuMapper.deleteByRoleId(roleId); //先删除当前角色id所有绑定关系
        //把前端传过来的菜单id数组绑定到当前这个角色id上去
        for (Integer menuId : menuIds) {
            Menu menu = menuService.getById(menuId); // 根据id找到菜单对象
            if (menu.getPid() != null && !menuIds.contains(menu.getPid())) {
                RoleMenu roleMenu = new RoleMenu(); // 创建一个新的角色
                roleMenu.setRole_id(roleId);
                // 此时子菜单有父级菜单，但父级菜单中无此子菜单
                roleMenu.setMenu_id(menu.getPid());

                roleMenuMapper.insert(roleMenu);
            }
            RoleMenu roleMenu = new RoleMenu(); // 创建一个新的角色
            //每个菜单都绑定一个角色
            roleMenu.setMenu_id(menuId);
            roleMenu.setRole_id(roleId);
            roleMenuMapper.insert(roleMenu);
        }
    }

    @Override
    public List<Integer> getRoleMenu(Integer roleId) {
        return roleMenuMapper.selectByRoleId(roleId);
    }
}
