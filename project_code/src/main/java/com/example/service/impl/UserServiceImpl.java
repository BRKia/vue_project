package com.example.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.Constants;
import com.example.entity.dto.UserDTO;
import com.example.entity.Menu;
import com.example.entity.User;
import com.example.exception.ServiceException;
import com.example.mapper.RoleMapper;
import com.example.mapper.RoleMenuMapper;
import com.example.mapper.UserMapper;
import com.example.service.IMenuService;
import com.example.service.IUserService;
import com.example.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author brk
 * @since 2022-06-27
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    private static final Log LOG = Log.get();
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private IMenuService menuService;
    @Override
    public UserDTO login(UserDTO userDTO) {
        User one = getUserInfo(userDTO);
        if(one != null) {
            BeanUtil.copyProperties(one, userDTO, true); //把user对象往参数对象里copy, 忽略大小写
            //设置token
            String token = TokenUtils.genToken(one.getId().toString(), one.getPassword());
            userDTO.setToken(token);
            String role = one.getRole();
            List<Menu> roleMenus = getRoleMenu(role);
            userDTO.setMenus(roleMenus);
            return userDTO;
        }else{
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
    }

    @Override
    public User register(UserDTO userDTO) {
        User one = getUserInfo(userDTO);
        if (one == null) {
            one = new User();
            BeanUtil.copyProperties(userDTO, one, true);
            save(one);  // 把 copy完之后的用户对象存储到数据库
        } else {
            throw new ServiceException(Constants.CODE_600, "用户已存在");
        }
        return one; //由于数据库存的就是一个user, 此处存储完直接返回存储对象即可
    }

    @Override
    public Page<User> findPage01(Page<User> page, String username, String nickname, String email, String address) {
        return userMapper.findPage01(page, username, nickname, email, address);
    }


    @Override
    public Page<User> findPage02(Page<User> page, String username, String nickname, String email, String address) {
        return userMapper.findPage02(page, username, nickname, email, address);
    }

    private User getUserInfo(UserDTO userDTO){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
//        queryWrapper.eq("password", userDTO.getPassword());
        User one;
        // 处理异常情况
        try {
            one = getOne(queryWrapper);
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误"); //此时为sql查询失败的问题
        }
        return one;
    }

    /**
     * 获取当前角色菜单页
     * @param role 角色名称
     * @return 查询到的角色所对应的菜单
     */
    private List<Menu> getRoleMenu(String role){
        Integer roleId = roleMapper.selectByFlag(role);
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);
        // 查出系统的所有菜单, 接着进行用户所具有的父菜单
        List<Menu> menus = menuService.findAll("");
        ArrayList<Menu> roleMenus = new ArrayList<>(); //最后筛选得到的菜单
        // 筛选当前用户角色所具有的菜单
        for (Menu menu : menus) {
            if(menuIds.contains(menu.getId())){
                roleMenus.add(menu);
            }
            // 查询出某一父菜单的所有子菜单
            List<Menu> children = menu.getChildren();
            // 删除角色对应的id，子菜单id不符的子菜单
            children.removeIf(child -> !menuIds.contains(child.getId()));
//            menu.setChildren(children);
        }
        return roleMenus;
    }
}
